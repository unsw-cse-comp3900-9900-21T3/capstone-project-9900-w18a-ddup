package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ProductVM {
    private String name;
    private String description;
    @PositiveOrZero
    private BigDecimal price;
    @PositiveOrZero
    private BigDecimal discountPrice;
    @Positive
    @Builder.Default
    private Long amount = 1L;

    private List<String> imagePaths;
}
