package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ProductVM {
    @NotBlank
    private String name;
    @Min(0)
    @NotNull
    private BigDecimal price;
    @Min(0)
    private BigDecimal discountPrice;
    @Min(1)
    private Long amount = 1L;
}
