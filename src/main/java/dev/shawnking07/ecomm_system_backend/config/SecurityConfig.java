package dev.shawnking07.ecomm_system_backend.config;

import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.jwt.JWTConfigurer;
import dev.shawnking07.ecomm_system_backend.security.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.Optional;
import java.util.stream.Collectors;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
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
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(new JWTConfigurer(tokenProvider));


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/static/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userRepository.findByEmailIgnoreCase(username);
            if (user.isEmpty()) {
                log.info("[Security] User [{}] has not register", username);
                throw new UsernameNotFoundException("You are not registered!");
            }
            User loginUser = user.get();
            return new org.springframework.security.core.userdetails.User(
                    loginUser.getEmail(),
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
