package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class RegisterDTO {
    @NotBlank
    @Size(min = 4, max = 50)
    @Schema(example = "jacky.w")
    private String username;
    @NotBlank
    @Schema(example = "Jack")
    private String firstname;
    @Schema(example = "W")
    private String lastname;
    @NotBlank
    @Size(min = 4, max = 100)
    @Schema(example = "example passwd")
    private String password;
    @Schema(example = "Sydney NSW 2052, Australia")
    private String address;
    @Schema(example = "2052")
    private String postcode;
    @Builder.Default
    private UserType userType = UserType.USER;

}
