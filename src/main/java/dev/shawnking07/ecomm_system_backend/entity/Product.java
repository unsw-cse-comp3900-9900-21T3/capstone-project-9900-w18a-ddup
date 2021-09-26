package dev.shawnking07.ecomm_system_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {
    private String name;
    @Min(0)
    private BigDecimal price;
    @Min(0)
    private BigDecimal discountPrice;
    private String picturePath;
    @Min(0)
    private Long amount;

    @ManyToMany
    @JoinTable(
            name = "products_tags",
            joinColumns = @JoinColumn(
                    name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;
}