package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class RegisterVM {
    @NotNull
    private String email;
    @NotNull
    private String firstname;
    private String lastname;
    @NotNull
    private String password;
    private String address;
    private String postcode;
    @NotNull
    private UserType userType;

    public enum UserType {
        ADMIN,
        USER
    }
}
