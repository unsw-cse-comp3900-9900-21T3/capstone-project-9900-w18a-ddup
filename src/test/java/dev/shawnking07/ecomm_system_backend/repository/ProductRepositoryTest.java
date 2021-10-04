package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Slf4j
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TagRepository tagRepository;

    private Long productId;

    private byte[] image;

    @BeforeEach
    public void addProduct() throws IOException {
        Tag tag = new Tag();
        tag.setName("Entertainment");
        tag.setCreatedBy("TESTER");

        Tag savedTag = tagRepository.save(tag);

        DbFile dbFile = new DbFile();
        dbFile.setCreatedBy("TESTER");
        Path path = Paths.get(".resources/README-md-1.png");
        image = Files.readAllBytes(path);
        dbFile.setContent(image);
        dbFile.setFilename("test-mask-markdown.png");
        dbFile.setFiletype(MediaType.IMAGE_PNG_VALUE);

        Product product = new Product();
        product.setName("test product");
        product.setPrice(new BigDecimal(100));
        product.setDiscountPrice(new BigDecimal(85));
        product.setTags(Set.of(savedTag));

        product.setCreatedBy("TESTER");
        product.setImages(List.of(dbFile));

        var save = productRepository.save(product);
        productId = save.getId();
        log.info("Saved product [{}]", productId);
    }

    @Test
    @Transactional
    public void testProductPicture() {
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product product = byId.get();
        Optional<DbFile> first = product.getImages().stream().findFirst();
        assertTrue(first.isPresent());
        assertArrayEquals(image, first.get().getContent());
    }

}