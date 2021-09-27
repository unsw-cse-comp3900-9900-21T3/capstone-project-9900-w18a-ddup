package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;

public interface UserService {
    void register(RegisterVM registerVM);

    void editUser(Long id, UserVM userVM);
}
