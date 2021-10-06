package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.OrderDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import dev.shawnking07.ecomm_system_backend.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final static String PRODUCT_AMOUNT = "product.amount.";
    private final static String ORDER_NUMBER = "order.number.";

    private final ProductRepository productRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final DbFileService dbFileService;
    private final StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public ProductServiceImpl(ProductRepository productRepository,
                              TagService tagService,
                              ModelMapper modelMapper,
                              DbFileService dbFileService,
                              StringRedisTemplate stringRedisTemplate) {
        this.productRepository = productRepository;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
        this.dbFileService = dbFileService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private List<DbFile> multipartFile2DbFile(List<MultipartFile> files) {
        return files.stream().map(f -> {
            DbFile dbFile = new DbFile();
            dbFile.setFilename(f.getName());
            dbFile.setFiletype(f.getContentType());
            try {
                dbFile.setContent(f.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
            return dbFile;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductVM addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Set<String> tags = productDTO.getTags();

        product.setTags(tags.stream().map(tagService::string2Tag).collect(Collectors.toSet()));

        List<DbFile> images = multipartFile2DbFile(productDTO.getFiles());
        product.setImages(images);
        productRepository.save(product);
        stringRedisTemplate.opsForValue().set(PRODUCT_AMOUNT + product.getId(), String.valueOf(product.getAmount()));
        return product2ProductVM(product);
    }

    //    @CacheEvict(cacheNames = "products", allEntries = true)
    @Transactional
    @Override
    public ProductVM editProduct(Long id, ProductDTO productDTO) {
        Product product = getProduct(id);
        modelMapper.map(productDTO, product);
        product.setImages(multipartFile2DbFile(productDTO.getFiles()));
        productRepository.save(product);
        stringRedisTemplate.opsForValue().set(PRODUCT_AMOUNT + product.getId(), String.valueOf(product.getAmount()));
        return product2ProductVM(product);
    }


    public Product getProduct(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id dose not exist");
        return byId.get();
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        stringRedisTemplate.delete(PRODUCT_AMOUNT + id);
    }

    @Transactional
    @Override
    public ProductVM queryProduct(Long id) {
        return product2ProductVM(getProduct(id));
    }

    @Override
    public void setProductAmountToCache(Long id, Long amount) {
        stringRedisTemplate.opsForValue().set(PRODUCT_AMOUNT + id, String.valueOf(amount));
    }

    @Override
    public Long getProductAmountFromCache(Long id) {
        String s = stringRedisTemplate.opsForValue().get(PRODUCT_AMOUNT + id);
        if (!NumberUtils.isParsable(s)) return null;
        return Long.parseLong(s);
    }

    @Override
    public Long getProductAmountFromCache(Long id, Long amount) {
        Long amount1 = getProductAmountFromCache(id);
        if (amount1 == null) {
            amount1 = amount;
            setProductAmountToCache(id, amount);
        }
        return amount1;
    }

    @Override
    public Long decreaseProductAmountInCache(Long id, long delta) {
        return stringRedisTemplate.opsForValue().decrement(PRODUCT_AMOUNT + id, delta);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Override
    public void correctProductAmountInCache() {
        log.info("Start correct Product amount in Redis.");
        Set<String> keys = redisTemplate.keys(ORDER_NUMBER + "*");
        if (keys == null) return;
        Map<Long, Long> productAmountInOrders = new HashMap<>();
        keys.forEach(v -> {
            OrderDTO order = (OrderDTO) redisTemplate.opsForHash().get(v, "order");
            if (order == null) return;
            List<OrderDTO.OrderProductsDTO> products = order.getProducts();
            products.forEach(p -> {
                Long orDefault = productAmountInOrders.getOrDefault(p.getProductId(), 0L);
                productAmountInOrders.put(p.getProductId(), orDefault + p.getAmount());
            });
        });

        Set<String> productKeys = stringRedisTemplate.keys(PRODUCT_AMOUNT + "*");
        if (productKeys == null) return;
        productKeys.stream().map(v -> {
            String s = StringUtils.substringAfterLast(v, ".");
            return NumberUtils.toLong(s);
        }).forEach(v -> {
            Long amountInOrders = productAmountInOrders.get(v);
            if (amountInOrders == null) return;
            Long cacheAmount = getProductAmountFromCache(v);
            if (cacheAmount == null) return;
            Optional<Product> byId = productRepository.findById(v);
            if (byId.isEmpty()) return;
            Product product = byId.get();
            Long dbAmount = product.getAmount();
            if (cacheAmount + amountInOrders != dbAmount) {
                setProductAmountToCache(v, dbAmount - amountInOrders);
            }
        });

        log.info("Correction done.");
    }

    @Transactional
    @Override
    public ProductVM product2ProductVM(Product product) {
        Long amount = getProductAmountFromCache(product.getId(), product.getAmount());
        var ppMap = modelMapper.typeMap(Product.class, ProductVM.ProductVMBuilder.class)
                .addMappings(mapping -> mapping.skip(ProductVM.ProductVMBuilder::tags));

        return ppMap.map(product)
                .tags(product.getTags().stream().map(tagService::tag2String).collect(Collectors.toSet()))
                .amount(amount)
                .imagePaths(product.getImages().stream().map(dbFileService::generateDownloadLink).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public List<ProductVM> listProducts() {
        return productRepository.findAll().stream()
                .map(this::product2ProductVM)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductVM> listProductsWithRecommendation() {
        // TODO: recommendation
        return null;
    }

    @Override
    public Page<ProductVM> listProductsWithRecommendation(Pageable pageable) {
        return null;
    }
}
