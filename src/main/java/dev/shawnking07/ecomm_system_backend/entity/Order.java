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

    public void addProduct(Product product, Long amount) {
        OrderProducts orderProducts = new OrderProducts();
        orderProducts.setProduct(product);
        orderProducts.setAmount(amount);
        this.products.add(orderProducts);
    }


    @Embeddable
    @ToString
    public static class OrderProducts {
        @OneToOne
        protected Product product;
        protected Long amount = 1L;

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }
    }

}
