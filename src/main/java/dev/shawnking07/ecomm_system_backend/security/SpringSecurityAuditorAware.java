package dev.shawnking07.ecomm_system_backend.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
    }
}
