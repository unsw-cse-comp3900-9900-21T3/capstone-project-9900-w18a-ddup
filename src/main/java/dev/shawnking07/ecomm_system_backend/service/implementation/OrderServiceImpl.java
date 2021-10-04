package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.OrderDTO;
import dev.shawnking07.ecomm_system_backend.dto.OrderVM;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.Order;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.OrderRepository;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.service.OrderService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import dev.shawnking07.ecomm_system_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final static String productAmountKey = "product.amount.";
    private final static String orderNumberKey = "order.number.";

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public OrderServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, ModelMapper modelMapper, ProductService productService, UserRepository userRepository, UserService userService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public OrderVM createOrder(OrderDTO orderDTO) {
        List<OrderVM.OrderProductsVM> productsVMS = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderDTO.OrderProductsDTO v : orderDTO.getProducts()) {
            Long currentAmount = (Long) redisTemplate.opsForValue().get(productAmountKey + v.getProductId());
            if (currentAmount == null || currentAmount < v.getAmount()) {
                log.info("Product [{}] is not enough", v.getProductId());
                throw new RuntimeException("Product is not enough");
            }
            redisTemplate.opsForValue().decrement(productAmountKey + v.getProductId(), v.getAmount());
            ProductVM product = productService.queryProduct(v.getProductId());
            if (v.getDiscount()) {
                // calculate discount price
                totalPrice = totalPrice.add(product.getDiscountPrice());
            } else {
                totalPrice = totalPrice.add(product.getPrice());
            }
            OrderVM.OrderProductsVM build = OrderVM.OrderProductsVM.builder()
                    .amount(v.getAmount())
                    .product(product)
                    .build();
            productsVMS.add(build);
        }

        OrderDTO orderWithPrice = orderDTO.toBuilder().totalPrice(totalPrice).build();

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(orderNumberKey + uuid, orderWithPrice);
        Optional<String> s = userRepository.findById(orderDTO.getPayerId()).map(User::getUsername);
        return OrderVM.builder()
                .orderNumber(uuid)
                .shippingAddress(orderDTO.getShippingAddress())
                .products(productsVMS)
                .totalPrice(totalPrice)
                .payer(s.orElse(""))
                .build();
    }

    @Transactional
    @Override
    public void confirmOrder(String orderNumber) {
        OrderDTO orderDTO = (OrderDTO) redisTemplate.opsForValue().get(orderNumberKey + orderNumber);
        if (orderDTO == null) throw new ResourceNotFoundException("orderNumber is wrong");
        Order order = modelMapper.map(orderDTO, Order.class);

        order.setPayer(userRepository.findById(orderDTO.getPayerId()).orElse(null));
        orderDTO.getProducts().forEach(v -> {
            var p = productRepository.findById(v.getProductId()).orElseThrow(ResourceNotFoundException::new);
            p.setAmount(p.getAmount() - v.getAmount());
            productRepository.save(p);
            order.addProduct(p, v.getAmount(), v.getDiscount());
        });
        User user = userService.getCurrentUser().orElseThrow(ResourceNotFoundException::new);
        user.addPurchasedOrder(order);
        if (orderDTO.getPayerId() != null) {
            User payer = userRepository.findById(orderDTO.getPayerId()).orElseThrow(ResourceNotFoundException::new);
            order.setPayer(payer);
        }

        orderRepository.save(order);
    }
}
