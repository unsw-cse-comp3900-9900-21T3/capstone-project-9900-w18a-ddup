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
public class RegisterVM {
    @NotNull
    @Size(min = 4, max = 50)
    private String email;
    @NotNull
    private String firstname;
    private String lastname;
    @NotNull
    @Size(min = 4, max = 100)
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
