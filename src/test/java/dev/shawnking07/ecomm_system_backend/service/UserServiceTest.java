package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.RegisterVM;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void register() {
        String email = "john.d@test.com";
        RegisterVM john = RegisterVM.builder()
                .firstname("John")
                .lastname("Doe")
                .email(email)
                .password("12345678")
                .userType(RegisterVM.UserType.USER)
                .build();
        userService.register(john);

        Optional<User> byEmailIgnoreCase = userRepository.findByEmailIgnoreCase(email);
        assertTrue(byEmailIgnoreCase.isPresent());
    }

    @Test
    void editUser() {
    }
}