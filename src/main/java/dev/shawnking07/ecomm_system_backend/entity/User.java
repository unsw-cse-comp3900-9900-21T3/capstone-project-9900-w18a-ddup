package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    private String firstname;
    private String lastname;
    @Column(unique = true)
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

}
