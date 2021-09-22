package dev.shawnking07.ecomm_system_backend;

import dev.shawnking07.ecomm_system_backend.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class ECommSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommSystemBackendApplication.class, args);
    }

}
