package dev.shawnking07.ecomm_system_backend.config;

import dev.shawnking07.ecomm_system_backend.entity.Privilege;
import dev.shawnking07.ecomm_system_backend.entity.Role;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.PrivilegeRepository;
import dev.shawnking07.ecomm_system_backend.repository.RoleRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This DataLoader can only be used in single node system.
 * If you need to deploy for multiple severs, use initialize data with SQL scripts instead.
 */
@Slf4j
@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    boolean alreadySetup;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.alreadySetup = false;
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> generalPrivilege = List.of(
                readPrivilege, writePrivilege);
        Role role_admin = createRoleIfNotFound("ROLE_ADMIN", generalPrivilege);
        Role role_user = createRoleIfNotFound("ROLE_USER", generalPrivilege);

        String email = "admin@test.com";
        Optional<User> byEmailIgnoreCase = userRepository.findByUsernameIgnoreCase(email);
        if (byEmailIgnoreCase.isPresent()) {
            log.info("[Init Data] (admin@test.com:admin) already exists!");
            return;
        }
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setUsername("admin@test.com");
        user.setRoles(Set.of(role_admin, role_user));
        user.setEnabled(true);
        userRepository.save(user);
        log.info("[Init Data] (admin@test.com:admin) created!");
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}