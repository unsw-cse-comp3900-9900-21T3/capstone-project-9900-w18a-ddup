package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Tag extends BaseEntity {
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    private String name;

    public Tag(String name) {
        this.name = name;
    }
}