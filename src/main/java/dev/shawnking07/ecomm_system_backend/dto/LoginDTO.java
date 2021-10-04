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
public class LoginDTO {
    @NotBlank
    @Size(min = 4, max = 50)
    @Schema(example = "admin@test.com")
    private String username;
    @NotBlank
    @Size(min = 4, max = 100)
    @Schema(example = "admin")
    private String password;
    @Schema(example = "false")
    private boolean rememberMe;
}
