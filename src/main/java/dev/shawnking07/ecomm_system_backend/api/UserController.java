package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.dto.UserDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
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
    @PatchMapping("/{id}")
    public void edit(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userService.editUser(id, userDTO);
    }

    @Operation(summary = "Edit user info for himself", description = "You should access this api with jwt token")
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping
    public void edit(@Valid @RequestBody UserDTO userDTO) {
        userService.editCurrentUser(userDTO);
    }

    @Operation(summary = "Get current user's info", description = "Username cannot be changed currently, each user has a unique ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Optional<UserVM> loadMyInfo() {
        return userService.loadMyInformation();
    }

}
