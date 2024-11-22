package oder_service.oder_service.dto;

import lombok.Data;
import java.util.List;
@Data
public class OrderRequest {
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}