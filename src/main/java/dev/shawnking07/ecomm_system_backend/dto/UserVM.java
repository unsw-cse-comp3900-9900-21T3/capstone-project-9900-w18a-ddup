package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UserVM {
    private Long id;
    private String firstname;
    private String lastname;
    private String address;
    private String postcode;
    private UserType userType;
}
