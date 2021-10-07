package dev.shawnking07.ecomm_system_backend.config;

import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.jwt.JWTConfigurer;
import dev.shawnking07.ecomm_system_backend.security.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EnableWebSecurity
@Import(SecurityProblemSupport.class)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfig(UserRepository userRepository, TokenProvider tokenProvider, SecurityProblemSupport problemSupport) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.problemSupport = problemSupport;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JWTConfigurer(tokenProvider))
                .and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/static/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html");
    }

    /**
     * Brute force cors
     *
     * @return cors
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Methods",
                "Access-Control-Allow-Headers", "Access-Control-Max-Age",
                "Access-Control-Request-Headers", "Access-Control-Request-Method"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
            if (user.isEmpty()) {
                log.info("[Security] User [{}] has not register", username);
                throw new UsernameNotFoundException("You are not registered!");
            }
            User loginUser = user.get();
            return new org.springframework.security.core.userdetails.User(
                    loginUser.getUsername(),
                    loginUser.getPassword(),
                    loginUser.isEnabled(),
                    true,
                    true,
                    true,
                    loginUser.getRoles().stream()
                            .map(v -> new SimpleGrantedAuthority(v.getName()))
                            .collect(Collectors.toList())
            );
        };
    }
}
