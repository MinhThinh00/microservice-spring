package inventory_service.inventory_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}