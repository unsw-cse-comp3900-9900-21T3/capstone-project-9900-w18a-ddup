package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Order extends BaseEntity {
    private String comments;
    @ManyToOne(optional = false)
    private User buyer;
    @ManyToOne(optional = false)
    private User payer;

    @ElementCollection
    private List<OrderProducts> products;
    @NotNull
    @Min(0)
    private BigDecimal totalPrice;
    private String shippingAddress;

    public void addProduct(Product product, Long amount, Boolean discount) {
        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setProduct(product);
        orderProducts.setAmount(amount);
        orderProducts.setDiscount(discount);
        this.products.add(orderProducts);
    }


    @Getter
    @Setter
    @Embeddable
    @ToString
    public static class OrderProducts {
        @OneToOne
        private Product product;
        private Long amount = 1L;
        private Boolean discount;
    }

}
