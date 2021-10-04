package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.dto.UserType;
import dev.shawnking07.ecomm_system_backend.entity.Role;
import dev.shawnking07.ecomm_system_backend.repository.RoleRepository;
import dev.shawnking07.ecomm_system_backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public UserType roles2UserType(Collection<Role> roles) {
        boolean admin = roles.stream().map(Role::getName).anyMatch(v -> v.contains("ADMIN"));
        return admin ? UserType.ADMIN : UserType.USER;
    }

    @Override
    public UserType stringRoles2UserType(Set<String> roles) {
        return roles.contains("ADMIN") ? UserType.ADMIN : UserType.USER;
    }

    @Override
    public Set<Role> userType2Roles(UserType userType) {
        Role role_user = roleRepository.findByName("ROLE_USER");
        if (userType == UserType.ADMIN) {
            return Set.of(roleRepository.findByName("ROLE_ADMIN"), role_user);
        }
        return Set.of(role_user);
    }
}
