package oder_service.oder_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name= "oder_items")
@Data
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
