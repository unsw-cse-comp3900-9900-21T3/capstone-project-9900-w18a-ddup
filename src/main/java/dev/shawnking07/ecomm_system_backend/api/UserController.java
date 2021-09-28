package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Edit user info for admin", description = "You should access this api with admin role")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "#userVM.email == T(dev.shawnking07.ecomm_system_backend.security.SecurityUtils).currentUserLogin.orElse('')")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @Valid @RequestBody UserVM userVM) {
        userService.editUser(id, userVM);
    }

    @Operation(summary = "Edit user info for himself", description = "You should access this api with jwt token")
    @PreAuthorize("#userVM.email == T(dev.shawnking07.ecomm_system_backend.security.SecurityUtils).currentUserLogin.orElse('')")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping()
    public void edit(@Valid @RequestBody UserVM userVM) {
        userService.editUser(null, userVM);
    }

}
