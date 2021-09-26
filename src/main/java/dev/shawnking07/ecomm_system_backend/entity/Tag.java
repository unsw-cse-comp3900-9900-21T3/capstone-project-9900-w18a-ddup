package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag extends BaseEntity {
    private String name;
    private Integer priorityOrder;
    @ManyToMany(mappedBy = "tags")
    private Collection<Product> products;
}