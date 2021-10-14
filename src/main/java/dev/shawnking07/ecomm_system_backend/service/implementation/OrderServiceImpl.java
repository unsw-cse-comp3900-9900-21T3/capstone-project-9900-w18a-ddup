package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.config.ApplicationProperties;
import dev.shawnking07.ecomm_system_backend.dto.DiscountVM;
import dev.shawnking07.ecomm_system_backend.dto.OrderDTO;
import dev.shawnking07.ecomm_system_backend.dto.OrderVM;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.Order;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.entity.User;
import dev.shawnking07.ecomm_system_backend.repository.OrderRepository;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.repository.UserRepository;
import dev.shawnking07.ecomm_system_backend.security.SecurityUtils;
import dev.shawnking07.ecomm_system_backend.service.OrderService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final static String ORDER_NUMBER = "order.number.";

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ApplicationProperties properties;
    private final RedisTemplate<String, Object> redisTemplate;

    public OrderServiceImpl(ProductRepository productRepository,
                            OrderRepository orderRepository,
                            ModelMapper modelMapper,
                            ProductService productService,
                            UserRepository userRepository,
                            ApplicationProperties properties,
                            RedisTemplate<String, Object> redisTemplate
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.userRepository = userRepository;
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    @Override
    public OrderVM createOrder(OrderDTO orderDTO) {
        List<OrderVM.OrderProductsVM> productsVMS = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderDTO.OrderProductsDTO v : orderDTO.getProducts()) {
            ProductVM product = productService.queryProduct(v.getProductId());
            if (product.getAmount() < v.getAmount()) {
                log.info("Product [{}] is not enough", v.getProductId());
                throw new RuntimeException("Product is not enough");
            }
            productService.decreaseProductAmountInCache(v.getProductId(), v.getAmount());
            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(v.getAmount())));
            OrderVM.OrderProductsVM build = OrderVM.OrderProductsVM.builder()
                    .amount(v.getAmount())
                    .product(product.toBuilder().amount(product.getAmount() - v.getAmount()).build())
                    .build();
            productsVMS.add(build);
        }

        OrderDTO orderWithPrice = orderDTO.toBuilder().totalPrice(totalPrice).discount(Boolean.FALSE).build();

        String uuid = UUID.randomUUID().toString();
        // cache orderDTO for confirm and set expire time
        var order = Map.of("order", orderWithPrice,
                "buyer", SecurityUtils.getCurrentUserLogin().orElse(""));
        redisTemplate.opsForHash().putAll(ORDER_NUMBER + uuid, order);
        redisTemplate.expire(ORDER_NUMBER + uuid, properties.getOrderExpire());

        String s = Optional.ofNullable(orderDTO.getPayerUsername()).orElse(SecurityUtils.getCurrentUserLogin().orElse(""));
        return OrderVM.builder()
                .orderNumber(uuid)
                .shippingAddress(orderDTO.getShippingAddress())
                .products(productsVMS)
                .totalPrice(totalPrice)
                .buyer(SecurityUtils.getCurrentUserLogin().orElse(""))
                .payer(s)
                .build();
    }

    @Override
    public DiscountVM operateDiscount(String orderNumber) {
        OrderDTO orderDTO = (OrderDTO) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "order");
        String buyerUsername = (String) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "buyer");

        if (orderDTO == null) throw new ResourceNotFoundException("orderNumber is wrong");
        if (SecurityUtils.getCurrentUserLogin().stream().noneMatch(v -> StringUtils.equals(buyerUsername, v))) {
            return DiscountVM.builder()
                    .productIds(orderDTO.getProducts().stream()
                            .map(OrderDTO.OrderProductsDTO::getProductId)
                            .collect(Collectors.toList()))
                    .build();
        }
        // update discount price
        BigDecimal totalPrice = new BigDecimal(0L);
        for (OrderDTO.OrderProductsDTO product : orderDTO.getProducts()) {
            Product product1 = productRepository.findById(product.getProductId()).orElseThrow(ResourceNotFoundException::new);
            BigDecimal discountPrice = product1.getDiscountPrice();
            totalPrice = totalPrice.add(discountPrice.multiply(new BigDecimal(product1.getAmount())));
        }
        OrderDTO build = orderDTO.toBuilder().totalPrice(totalPrice).discount(Boolean.TRUE).build();
        redisTemplate.opsForHash().put(ORDER_NUMBER + orderNumber, "order", build);
        return DiscountVM.builder()
                .orderNumber(orderNumber)
                .productIds(orderDTO.getProducts().stream()
                        .map(OrderDTO.OrderProductsDTO::getProductId)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public OrderVM queryOrder(String orderNumber) {
        OrderDTO orderDTO = (OrderDTO) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "order");
        String buyerUsername = (String) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "buyer");
        if (orderDTO == null) throw new ResourceNotFoundException("orderNumber is wrong");

        List<OrderVM.OrderProductsVM> productsVMS = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderDTO.OrderProductsDTO v : orderDTO.getProducts()) {
            ProductVM product = productService.queryProduct(v.getProductId());
            if (product.getAmount() < v.getAmount()) {
                log.info("Product [{}] is not enough", v.getProductId());
                throw new RuntimeException("Product is not enough");
            }
            var price = orderDTO.getDiscount() ? product.getDiscountPrice() : product.getPrice();
            totalPrice = totalPrice.add(price.multiply(new BigDecimal(v.getAmount())));
            OrderVM.OrderProductsVM build = OrderVM.OrderProductsVM.builder()
                    .amount(v.getAmount())
                    .product(product.toBuilder()
                            .amount(productService.getProductAmountFromCache(product.getId()))
                            .build())
                    .build();
            productsVMS.add(build);
        }

        return OrderVM.builder()
                .orderNumber(orderNumber)
                .shippingAddress(orderDTO.getShippingAddress())
                .products(productsVMS)
                .totalPrice(totalPrice)
                .buyer(buyerUsername)
                .payer(orderDTO.getPayerUsername())
                .discount(orderDTO.getDiscount())
                .build();
    }

    @Transactional
    @Override
    public void confirmOrder(String orderNumber) {
        OrderDTO orderDTO = (OrderDTO) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "order");
        String buyerUsername = (String) redisTemplate.opsForHash().get(ORDER_NUMBER + orderNumber, "buyer");

        if (orderDTO == null) throw new ResourceNotFoundException("orderNumber is wrong");
        Order order = modelMapper.map(orderDTO, Order.class);

        User buyer = userRepository.findByUsernameIgnoreCase(buyerUsername).orElseThrow(ResourceNotFoundException::new);
        order.setBuyer(buyer);
        User payer = userRepository.findByUsernameIgnoreCase(orderDTO.getPayerUsername())
                .orElse(userRepository.findByUsernameIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Security Fails"))).orElseThrow());
        order.setPayer(payer);

        orderDTO.getProducts().forEach(v -> {
            var p = productRepository.findById(v.getProductId()).orElseThrow(ResourceNotFoundException::new);
            Long productAmountFromCache = productService.getProductAmountFromCache(v.getProductId());
            p.setAmount(productAmountFromCache != null ? productAmountFromCache : p.getAmount() - v.getAmount());
            productRepository.save(p);
            order.addProduct(p, v.getAmount());
        });

        buyer.addPurchasedOrder(order);
        payer.addPaidOrder(order);

        userRepository.save(buyer);
        userRepository.save(payer);

        redisTemplate.delete(ORDER_NUMBER + orderNumber);
        orderRepository.save(order);
    }

    @Override
    public OrderVM order2orderVM(Order order) {
        var ooMap = modelMapper.typeMap(Order.class, OrderVM.OrderVMBuilder.class);
        ooMap.addMappings(mapping -> mapping.skip(OrderVM.OrderVMBuilder::products));
        return ooMap.map(order)
                .buyer(order.getBuyer().getUsername())
                .payer(order.getPayer().getUsername())
                .products(order.getProducts().stream().map(v -> OrderVM.OrderProductsVM.builder()
                        .product(productService.product2ProductVM(v.getProduct()))
                        .amount(v.getAmount())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
