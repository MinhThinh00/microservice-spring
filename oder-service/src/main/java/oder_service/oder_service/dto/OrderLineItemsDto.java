package oder_service.oder_service.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderLineItemsDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}