package dev.shawnking07.ecomm_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.http.MediaType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    private String filename;

    private String filetype;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;
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


    public byte[] getPicture() {
        return Arrays.copyOf(picture, picture.length);
    }

    public void setPicture(byte[] picture) {
        this.picture = Arrays.copyOf(picture, picture.length);
    }

    public List<MediaType> getFiletype() {
        return MediaType.parseMediaTypes(filetype);
    }

    public void setFiletype(Collection<MediaType> mediaTypes) {
        this.filetype = MediaType.toString(mediaTypes);
    }
}