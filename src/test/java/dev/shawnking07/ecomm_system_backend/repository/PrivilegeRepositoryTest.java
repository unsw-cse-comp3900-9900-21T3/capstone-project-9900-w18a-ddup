package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Privilege;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PrivilegeRepositoryTest {
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Test
    void savePrivilege() {
        Privilege test = new Privilege("TEST");
        privilegeRepository.save(test);
        assertEquals(1L, test.getId());
    }
}