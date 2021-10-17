package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Jacksonized
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OrderDTO {
    @NotNull
    @Valid
    private List<OrderProductsDTO> products;
    private String payerUsername;
    @NotBlank
    private String shippingAddress;
    @Schema(hidden = true)
    private BigDecimal totalPrice;
    @Schema(hidden = true)
    @Builder.Default
    private Boolean discount = Boolean.FALSE;

    @Jacksonized
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class OrderProductsDTO {
        @NotNull
        private Long productId;
        @Builder.Default
        @Positive
        private Long amount = 1L;
    }
}
