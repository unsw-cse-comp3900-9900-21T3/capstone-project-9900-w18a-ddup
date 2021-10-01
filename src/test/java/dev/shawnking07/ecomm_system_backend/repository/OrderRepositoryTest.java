package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Order;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.entity.Tag;
import dev.shawnking07.ecomm_system_backend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    private Long orderId;

    @BeforeEach
    public void addProductAndInit() {
        Tag tag = new Tag();
        tag.setName("Entertainment");
        tag.setCreatedBy("TESTER");

        Tag savedTag = tagRepository.save(tag);

        Product product = new Product();
        product.setName("test product");
        product.setPrice(new BigDecimal(100));
        product.setDiscountPrice(new BigDecimal(85));
        product.setTags(Set.of(savedTag));
//        product.setFilename("test-mask-markdown.png");
//        product.setFiletype(List.of(MediaType.IMAGE_PNG));
        product.setCreatedBy("TESTER");
        productRepository.save(product);
        Long productId = product.getId();
        log.info("Saved product [{}]", productId);
        orderId = createOrder(productId);
    }

    public Long createOrder(Long productId) {
        Order order = new Order();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product product = byId.get();
        Order.OrderProducts orderProducts = new Order.OrderProducts();
        orderProducts.setProduct(product);
        orderProducts.setAmount(2L);
        order.setProducts(List.of(orderProducts));

        User user = new User();
        user.setEmail("dd@test.com");
        user.setFirstname("tester1");
        user.setCreatedBy("TESTER");
        userRepository.save(user);

        order.setBuyer(user);
        order.setPayer(user);
        order.setTotalPrice(new BigDecimal(200));
        order.setCreatedBy("TESTER");

        orderRepository.save(order);
        log.info("Saved order");
        return order.getId();
    }

    @Test
    public void testOrder() {

        Order order = orderRepository.getById(orderId);
        assertEquals(new BigDecimal(200), order.getTotalPrice());
        assertEquals("dd@test.com", order.getBuyer().getEmail());

        var products = order.getProducts();
        assertEquals(1, products.size());

        List<Order.OrderProducts> list = new ArrayList<>(order.getProducts());
        Order.OrderProducts orderProducts = list.get(0);

        Product product = orderProducts.getProduct();
        assertEquals(new BigDecimal(100), product.getPrice());

    }

    @Test
    public void testUserOrder() {
        Optional<User> byEmailIgnoreCase = userRepository.findByEmailIgnoreCase("dd@test.com");
        assertTrue(byEmailIgnoreCase.isPresent());
        User user = byEmailIgnoreCase.get();

        List<Order> purchases = user.getPurchases();
        if (purchases.size() == 0) {
            purchases = user.getPurchases();
        }
        assertEquals(1, purchases.size());
    }

}