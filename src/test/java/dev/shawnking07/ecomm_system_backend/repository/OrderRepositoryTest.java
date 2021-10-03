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
import java.util.*;

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
        user.setUsername("dd@test.com");
        user.setFirstname("tester1");
        user.setCreatedBy("TESTER");
        userRepository.saveAndFlush(user);

        order.setBuyer(user);
        order.setPayer(user);
        order.setTotalPrice(new BigDecimal(200));
        order.setCreatedBy("TESTER");

        user.addPaidOrder(order);
        user.addPurchasedOrder(order);
//        userRepository.saveAndFlush(user);
        orderRepository.save(order);
        log.info("Saved order, buyer: [{}]", order.getBuyer().getUsername());
        log.info("[Order info]: {}", order);
        return order.getId();
    }

    @Test
    public void testOrder() {

        Order order = orderRepository.getById(orderId);
        assertEquals(new BigDecimal(200), order.getTotalPrice());
        assertEquals("dd@test.com", order.getBuyer().getUsername());

        var products = order.getProducts();
        assertEquals(1, products.size());

        List<Order.OrderProducts> list = new ArrayList<>(order.getProducts());
        Order.OrderProducts orderProducts = list.get(0);

        Product product = orderProducts.getProduct();
        assertEquals(new BigDecimal(100), product.getPrice());

    }

    @Test
    public void testUserOrder() {
        Optional<User> byEmailIgnoreCase = userRepository.findByUsernameIgnoreCase("dd@test.com");
        assertTrue(byEmailIgnoreCase.isPresent());
        User user = byEmailIgnoreCase.get();

        Collection<Order> purchases = user.getPurchases();
        assertEquals(1, purchases.size());
    }

}