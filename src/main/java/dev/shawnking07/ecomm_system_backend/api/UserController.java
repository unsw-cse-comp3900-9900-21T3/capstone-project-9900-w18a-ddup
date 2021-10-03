package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Edit user info for admin", description = "You should access this api with admin role")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @Valid @RequestBody UserVM userVM) {
        userService.editUser(id, userVM);
    }

    @Operation(summary = "Edit user info for himself", description = "You should access this api with jwt token")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping()
    public void edit(@Valid @RequestBody UserVM userVM) {
        userService.editCurrentUser(userVM);
    }

    @Operation(summary = "Get current user's info")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Optional<User> loadMyInfo() {
        return userService.loadMyInformation();
    }

}
