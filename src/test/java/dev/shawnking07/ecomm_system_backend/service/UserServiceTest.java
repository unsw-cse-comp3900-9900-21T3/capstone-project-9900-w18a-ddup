package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.RegisterDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserDTO;
import dev.shawnking07.ecomm_system_backend.dto.UserType;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long userId;

    @BeforeEach
    void register() {
        String email = "john.d@test.com";
        RegisterDTO john = RegisterDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .username(email)
                .password("12345678")
                .userType(UserType.USER)
                .build();
        userService.register(john);

        log.info("[Register] user [{}] created!", email);

        Optional<User> byEmailIgnoreCase = userRepository.findByUsernameIgnoreCase(email);
        assertTrue(byEmailIgnoreCase.isPresent());
        User user = byEmailIgnoreCase.get();

        userId = user.getId();

        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals(email, user.getUsername());
        assertTrue(user.isEnabled());
        assertTrue(passwordEncoder.matches("12345678", user.getPassword()));
    }

    @Test
    @WithMockUser(username = "john.d@test.com")
    @Transactional
    void editMyself() {
        String address = "new address test";
        UserDTO userDTO = UserDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .password(passwordEncoder.encode("11223344"))
                .address(address)
                .build();
        userService.editCurrentUser(userDTO);

        Optional<User> byEmailIgnoreCase = userRepository.findByUsernameIgnoreCase("john.d@test.com");
        assertTrue(byEmailIgnoreCase.isPresent());
        User user = byEmailIgnoreCase.get();
        assertEquals(address, user.getAddress());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"admin"})
    @Transactional
    void adminEditUser() {
        String address = "new address test";
        UserDTO userDTO = UserDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .password(passwordEncoder.encode("11223344"))
                .address(address)
                .build();
        userService.editUser(userId, userDTO);

        Optional<User> byEmailIgnoreCase = userRepository.findByUsernameIgnoreCase("john.d@test.com");
        assertTrue(byEmailIgnoreCase.isPresent());
        User user = byEmailIgnoreCase.get();
        assertEquals(address, user.getAddress());
    }
}