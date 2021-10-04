package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.RegisterDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.User;

import java.util.Optional;

public interface UserService {
    void register(RegisterDTO registerDTO);

    /**
     * extract SecurityContext User
     *
     * @return User
     */
    Optional<User> getCurrentUser();

    Long getCurrentUserId();

    Optional<User> editUser(Long id, UserDTO userDTO);

    Optional<User> editCurrentUser(UserDTO userDTO);

    /**
     * load all current user's information, except the order infos
     *
     * @return User
     */
    Optional<UserVM> loadMyInformation();
}
