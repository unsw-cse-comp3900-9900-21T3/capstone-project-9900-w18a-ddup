package dev.shawnking07.ecomm_system_backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.shawnking07.ecomm_system_backend.dto.LoginVM;
import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.security.jwt.JWTFilter;
import dev.shawnking07.ecomm_system_backend.security.jwt.TokenProvider;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    public UserController(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Operation(summary = "Login system and return jwt token string",
            description = "this may be the only api does not satisfy RESTful")
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginVM) {
        var token = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        var authentication = authenticationManagerBuilder.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @Operation(summary = "User registration", description = "You can access this api without any authorization")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public void register(@Valid @RequestBody RegisterVM registerVM) {
        userService.register(registerVM);
    }

    @Operation(summary = "Edit user info for admin", description = "You should access this api with admin role")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "#userVM.email == T(dev.shawnking07.ecomm_system_backend.security.SecurityUtils).currentUserLogin.orElse('')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public void edit(@Valid @RequestBody UserVM userVM, @PathVariable Long id) {
        userService.editUser(id, userVM);
    }

    @Operation(summary = "Edit user info for himself", description = "You should access this api with jwt token")
    @PreAuthorize("#userVM.email == T(dev.shawnking07.ecomm_system_backend.security.SecurityUtils).currentUserLogin.orElse('')")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/")
    public void edit(@Valid @RequestBody UserVM userVM) {
        userService.editUser(null, userVM);
    }

}
