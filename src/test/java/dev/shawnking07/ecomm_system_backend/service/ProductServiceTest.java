package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.ECommTestConfiguration;
import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Import(ECommTestConfiguration.class)
class ProductServiceTest {
    private static ProductDTO productDTO;
    @Autowired
    private ProductService productService;
    private Long id;

    @BeforeAll
    static void createDTO() {
        productDTO = new ProductDTO();
        productDTO.setName("AirPods Pro");
        productDTO.setDescription("Earphone made by Apple");
        productDTO.setAmount(200L);
        productDTO.setPrice(new BigDecimal("499.99"));
        productDTO.setDiscountPrice(new BigDecimal("459.99"));
        productDTO.setTags(Set.of("Apple", "Earphone"));
    }

    @BeforeEach
    void addProduct() {
        ProductVM productVM = productService.addProduct(productDTO);
        assertNotNull(productVM);
        assertNotNull(productVM.getId());
        id = productVM.getId();
    }

    @Test
    @Transactional
    public void editProduct() {
        log.info("[edit product]");
        ProductDTO edit = new ProductDTO();
        BeanUtils.copyProperties(productDTO, edit);
        edit.setPrice(new BigDecimal("450"));
        Set<String> tags = Set.of("Not Apple");
        edit.setTags(tags);
        ProductVM productVM = productService.editProduct(id, edit);

        assertEquals(id, productVM.getId());
        assertEquals(new BigDecimal("450"), productVM.getPrice());
        assertEquals(tags, productVM.getTags());
    }

    @Test
    @Transactional
    void deleteProduct() {
        log.info("[delete product]");
        productService.deleteProduct(id);
        assertThrows(ResourceNotFoundException.class, () -> productService.queryProduct(id));

    }
}