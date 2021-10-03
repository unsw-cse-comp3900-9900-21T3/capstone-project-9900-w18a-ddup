package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.BaseEntity;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.RoleRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.SecurityUtils;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

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
        return userRepository.findByUsernameIgnoreCase(email).isPresent();
    }

    @Override
    public void register(RegisterVM registerVM) {
        User user = modelMapper.map(registerVM, User.class);
        if (duplicateUser(user.getUsername())) {
            throw new RuntimeException("Duplicate user: " + registerVM.getUsername());
        }
        user.setPassword(passwordEncoder.encode(registerVM.getPassword()));
        if (registerVM.getUserType() == RegisterVM.UserType.ADMIN) {
            user.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN")));
        } else {
            user.setRoles(Set.of(roleRepository.findByName("ROLE_USER")));
        }
        userRepository.save(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) return Optional.empty();
        String s = currentUserLogin.get();
        return userRepository.findByUsernameIgnoreCase(s);
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().map(BaseEntity::getId).orElse(null);
    }

    @Override
    public Optional<User> editUser(Long id, @NotNull UserVM userVM) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RuntimeException("id does not exist");
        }
        User user = byId.get();
        modelMapper.map(userVM, user);
        user.setId(id);
        if (StringUtils.isNotBlank(userVM.getPassword()))
            user.setPassword(passwordEncoder.encode(userVM.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> editCurrentUser(UserVM userVM) {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            throw new RuntimeException("Account error, you need to re-login");
        }
        User user = currentUser.get();
        modelMapper.map(userVM, user);
        if (StringUtils.isNotBlank(userVM.getPassword()))
            user.setPassword(passwordEncoder.encode(userVM.getPassword()));

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> loadMyInformation() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) return Optional.empty();
        return userRepository.findByUsernameIgnoreCase(currentUserLogin.get());
    }
}
