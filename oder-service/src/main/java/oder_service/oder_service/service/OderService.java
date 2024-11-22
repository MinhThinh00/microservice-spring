package oder_service.oder_service.service;


import oder_service.oder_service.dto.InventoryResponse;
import oder_service.oder_service.dto.OrderLineItemsDto;
import oder_service.oder_service.dto.OrderRequest;
import oder_service.oder_service.event.OrderPlacedEvent;
import oder_service.oder_service.model.Oder;
import oder_service.oder_service.model.OrderLineItems;
import oder_service.oder_service.repository.OderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
public class OderService {
    private final OderRepository oderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public OderService(OderRepository oderRepository, WebClient.Builder webClientBuilder, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.oderRepository = oderRepository;
        this.webClientBuilder = webClientBuilder;

        this.kafkaTemplate = kafkaTemplate;
    }
    public String placeOder(OrderRequest orderRequest){
        Oder oder= new Oder();
        oder.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDto).toList();
        oder.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes=oder.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
        // Call Inventory Service, and place order if product is in
        // stock
        InventoryResponse[] inventoryResponses= webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class)
                                                .block();
        boolean  allProductIsInstock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if(allProductIsInstock){
            oderRepository.save(oder);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(oder.getOrderNumber()));
            return "Order Placed Successfully";
        }
        else {
            throw new RuntimeException("Product is not in stock");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
