package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.shawnking07.ecomm_system_backend.entity.Order;
import lombok.Builder;
import lombok.Getter;
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
    private List<Order.OrderProducts> products;
    private BigDecimal totalPrice;
    private String shippingAddress;
}
