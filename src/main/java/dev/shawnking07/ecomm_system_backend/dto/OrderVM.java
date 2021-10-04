package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OrderVM {
    private Long id;
    private String comments;
    private List<OrderProductsVM> products;
    private BigDecimal totalPrice;
    private String shippingAddress;

    @Getter
    @Setter
    public static class OrderProductsVM {
        private ProductVM product;
        private Long amount;
    }
}
