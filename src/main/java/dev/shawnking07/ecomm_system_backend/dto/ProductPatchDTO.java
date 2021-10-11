package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ProductPatchDTO {
    @Schema(example = "AirPods Max")
    private String name;
    @Schema(example = "What about Sony XM4")
    private String description;
    @Schema(example = "499.99")
    @PositiveOrZero
    private BigDecimal price;
    @Schema(example = "459.99")
    @PositiveOrZero
    private BigDecimal discountPrice;
    @Schema(example = "3000")
    @Positive
    private Long amount = 1L;
    @ArraySchema(uniqueItems = true, schema = @Schema(example = "Apple"))
    private Set<String> tags;
    private List<MultipartFile> files;
}
