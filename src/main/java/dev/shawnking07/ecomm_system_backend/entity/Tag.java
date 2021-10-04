package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @JsonIgnore
    private Collection<Product> products;

    private String name;

    public Tag(String name) {
        this.name = name;
    }
}