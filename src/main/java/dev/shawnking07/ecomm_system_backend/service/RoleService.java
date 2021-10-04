package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.UserType;
import dev.shawnking07.ecomm_system_backend.entity.Role;

import java.util.Collection;
import java.util.Set;

public interface RoleService {
    UserType roles2UserType(Collection<Role> roles);

    UserType stringRoles2UserType(Set<String> roles);

    Set<Role> userType2Roles(UserType userType);
}
