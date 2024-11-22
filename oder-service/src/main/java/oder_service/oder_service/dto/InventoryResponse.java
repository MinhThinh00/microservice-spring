package oder_service.oder_service.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}