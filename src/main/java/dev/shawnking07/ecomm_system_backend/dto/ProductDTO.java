package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ProductDTO {
    @NotBlank
    private String name;
    private String description;
    @PositiveOrZero
    private BigDecimal price;
    @PositiveOrZero
    private BigDecimal discountPrice;
    @Positive
    private Long amount = 1L;
    private Set<String> tags;
    private List<MultipartFile> files;
}
