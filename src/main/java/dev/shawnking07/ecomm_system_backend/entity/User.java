package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.lang.Collections;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity {
    private String firstname;
    private String lastname;
    @Column(unique = true, length = 50)
    private String email;
    @JsonIgnore
    private String password;
    private boolean enabled = Boolean.TRUE;
    private String address;
    @Size(max = 20)
    private String postcode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    @ToString.Exclude
    private Collection<Order> purchases;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payer")
    @ToString.Exclude
    private Collection<Order> paidOrders;

    public void addPurchasedOrder(Order order) {
        if (Collections.isEmpty(purchases)) {
            purchases = new ArrayList<>();
        }
        purchases.add(order);
    }

    public void addPaidOrder(Order order) {
        if (Collections.isEmpty(paidOrders)) {
            paidOrders = new ArrayList<>();
        }
        paidOrders.add(order);
    }

}
