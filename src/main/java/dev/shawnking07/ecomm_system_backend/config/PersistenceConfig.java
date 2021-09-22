package dev.shawnking07.ecomm_system_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"dev.shawnking07.ecomm_system_backend.repository"})
@EnableJpaAuditing
public class PersistenceConfig {
}
