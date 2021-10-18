package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;

@Jacksonized
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OrderNumberDTO {
    @NotEmpty
    private String orderNumber;
}
