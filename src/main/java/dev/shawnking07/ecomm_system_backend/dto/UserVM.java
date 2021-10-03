package dev.shawnking07.ecomm_system_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Size;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class UserVM {
    @Schema(example = "Jacky")
    private String firstname;
    @Schema(example = "Wong")
    private String lastname;
    @Size(min = 4, max = 100)
    @Schema(example = "my name is jacky.w")
    private String password;
    private boolean enabled;
    @Schema(example = "Kensington NSW, AU")
    private String address;
    @Schema(example = "2000")
    private String postcode;
//    private Set<Long> roles;
}
