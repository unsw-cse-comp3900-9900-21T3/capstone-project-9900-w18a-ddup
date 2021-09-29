package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UserVM {
    @NotNull
    private String firstname;
    private String lastname;
    @NotNull
    @Size(min = 4, max = 50)
    private String email;
    @NotNull
    @Size(min = 4, max = 100)
    private String password;
    private boolean enabled;
    private String address;
    private String postcode;
//    private Set<Long> roles;
}
