package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.User;

import java.util.Optional;

public interface UserService {
    void register(RegisterVM registerVM);

    /**
     * extract SecurityContext User
     *
     * @return User
     */
    Optional<User> getCurrentUser();

    Long getCurrentUserId();

    Optional<User> editUser(Long id, UserVM userVM);

    Optional<User> editCurrentUser(UserVM userVM);

    /**
     * load all current user's information, except the order infos
     *
     * @return User
     */
    Optional<User> loadMyInformation();
}
