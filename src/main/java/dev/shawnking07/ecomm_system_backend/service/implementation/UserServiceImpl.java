package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.RoleRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.SecurityUtils;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean duplicateUser(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    @Override
    public void register(RegisterVM registerVM) {
        User user = modelMapper.map(registerVM, User.class);
        if (duplicateUser(user.getEmail())) {
            throw new RuntimeException("Duplicate user");
        }
        user.setPassword(passwordEncoder.encode(registerVM.getPassword()));
        if (registerVM.getUserType() == RegisterVM.UserType.ADMIN) {
            user.setRoles(List.of(roleRepository.findByName("ROLE_ADMIN")));
        } else {
            user.setRoles(List.of(roleRepository.findByName("ROLE_USER")));
        }
        userRepository.save(user);
    }

    @Override
    public void editUser(Long id, @NotNull UserVM userVM) {
        User user = modelMapper.map(userVM, User.class);
        if (id == null && SecurityUtils.getCurrentUserLogin().isPresent()) {
            String username = SecurityUtils.getCurrentUserLogin().get();
            Optional<User> byEmailIgnoreCase = userRepository.findByEmailIgnoreCase(username);
            if (byEmailIgnoreCase.isPresent()) {
                id = byEmailIgnoreCase.get().getId();
            }
        }
        user.setId(id);
        userRepository.save(user);
    }
}
