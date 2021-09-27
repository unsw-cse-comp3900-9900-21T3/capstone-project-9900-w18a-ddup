package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order extends BaseEntity {
    private String comments;
    @OneToOne
    private User buyer;
    @OneToOne
    private User payer;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<OrderProducts> products;

    @Embeddable
    @Setter
    @Getter
    @NoArgsConstructor
    static class OrderProducts {
        @OneToOne
        private Product product;
        private Long amount = 1L;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderProducts that = (OrderProducts) o;
            return product.equals(that.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product);
        }
    }

}
