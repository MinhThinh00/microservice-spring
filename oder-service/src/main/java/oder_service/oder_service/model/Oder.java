package oder_service.oder_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name= "t_oder")
@Data
public class Oder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItemsList;
}
