package dev.shawnking07.ecomm_system_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import java.time.Duration;

@ConfigurationProperties(prefix = "e-comm")
@Setter
@Getter
public class ApplicationProperties {
    private final Jwt jwt = new Jwt();
    private final Duration orderExpire = Duration.ofMinutes(15);
    private final Duration downloadLinkExpire = Duration.ofMinutes(10);

    @Setter
    @Getter
    public static class Jwt {
        private String secret;
        @Min(1000)
        private Duration tokenValidity = Duration.ofDays(1);
        @Min(1000)
        private Duration tokenValidityForRememberMe = Duration.ofDays(7);
    }
}
