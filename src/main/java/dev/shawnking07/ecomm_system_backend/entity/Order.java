package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity {
    private String comments;
    @ManyToOne
    private User buyer;
    @ManyToOne
    private User payer;

    @ElementCollection
    private List<OrderProducts> products = new ArrayList<>();

    private BigDecimal totalPrice;
    private String shippingAddress;


    @Embeddable
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
