package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Tag extends BaseEntity {
    private String name;
    private Integer priorityOrder;
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    private Collection<Product> products;
}