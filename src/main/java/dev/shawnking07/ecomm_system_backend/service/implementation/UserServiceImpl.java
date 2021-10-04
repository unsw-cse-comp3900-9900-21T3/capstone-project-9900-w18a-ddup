package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.dto.RegisterDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserVM;
import dev.shawnking07.ecomm_system_backend.entity.BaseEntity;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.RoleRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.SecurityUtils;
import dev.shawnking07.ecomm_system_backend.service.RoleService;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    private boolean duplicateUser(String email) {
        return userRepository.findByUsernameIgnoreCase(email).isPresent();
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        User user = modelMapper.map(registerDTO, User.class);
        if (duplicateUser(user.getUsername())) {
            throw new RuntimeException("Duplicate user: " + registerDTO.getUsername());
        }
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(roleService.userType2Roles(registerDTO.getUserType()));
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
    public Optional<User> editUser(Long id, @NotNull UserDTO userDTO) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RuntimeException("id does not exist");
        }
        User user = byId.get();
        modelMapper.map(userDTO, user);
        user.setId(id);
        if (StringUtils.isNotBlank(userDTO.getPassword()))
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> editCurrentUser(UserDTO userDTO) {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            throw new RuntimeException("Account error, you need to re-login");
        }
        User user = currentUser.get();
        modelMapper.map(userDTO, user);
        if (StringUtils.isNotBlank(userDTO.getPassword()))
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return Optional.of(userRepository.save(user));
    }

    @Transactional
    @Override
    public Optional<UserVM> loadMyInformation() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) return Optional.empty();
//        var userTypeMapper = modelMapper.typeMap(User.class, UserVM.UserVMBuilder.class);
        // TODO: lazy load for orders, needs to be removed
//        userTypeMapper.addMappings(mapping -> {
//            mapping.skip(UserVM.UserVMBuilder::purchases);
//            mapping.skip(UserVM.UserVMBuilder::paidOrders);
//        });
        return userRepository.findByUsernameIgnoreCase(currentUserLogin.get())
                .map(v -> modelMapper.map(v, UserVM.UserVMBuilder.class)
                        .userType(roleService.roles2UserType(v.getRoles()))
                        .build());
    }
}
