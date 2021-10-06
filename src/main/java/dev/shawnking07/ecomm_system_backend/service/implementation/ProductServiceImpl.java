package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import dev.shawnking07.ecomm_system_backend.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final static String PRODUCT_AMOUNT = "product.amount.";

    private final ProductRepository productRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final DbFileService dbFileService;
    private final StringRedisTemplate redisTemplate;

    public ProductServiceImpl(ProductRepository productRepository, TagService tagService, ModelMapper modelMapper, DbFileService dbFileService, StringRedisTemplate redisTemplate) {
        this.productRepository = productRepository;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
        this.dbFileService = dbFileService;
        this.redisTemplate = redisTemplate;
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
//    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public ProductVM addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Set<String> tags = productDTO.getTags();

        product.setTags(tags.stream().map(tagService::string2Tag).collect(Collectors.toSet()));

        List<DbFile> images = multipartFile2DbFile(productDTO.getFiles());
        product.setImages(images);
        productRepository.save(product);
        redisTemplate.opsForValue().set(PRODUCT_AMOUNT + product.getId(), String.valueOf(product.getAmount()));
        return product2ProductVM(product);
    }

    //    @CacheEvict(cacheNames = "products", allEntries = true)
    @Transactional
    @Override
    public ProductVM editProduct(Long id, ProductDTO productDTO) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id dose not exist");
        var product = byId.get();
        modelMapper.map(productDTO, product);
        product.setImages(multipartFile2DbFile(productDTO.getFiles()));
        productRepository.save(product);
        redisTemplate.opsForValue().set(PRODUCT_AMOUNT + product.getId(), String.valueOf(product.getAmount()));
        return product2ProductVM(product);
    }

    //    @CacheEvict(cacheNames = "products", allEntries = true)
    @Transactional
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        redisTemplate.delete(PRODUCT_AMOUNT + id);
    }

    @Transactional
    @Override
    public ProductVM queryProduct(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id does not exist");
        return product2ProductVM(byId.get());
    }

    @Override
    public void setProductAmountToCache(Long id, Long amount) {
        redisTemplate.opsForValue().set(PRODUCT_AMOUNT + id, String.valueOf(amount));
    }

    @Override
    public Long getProductAmountFromCache(Long id) {
        String s = redisTemplate.opsForValue().get(PRODUCT_AMOUNT + id);
        if (s == null) return null;
        return Long.parseLong(s);
    }

    @Transactional
    @Override
    public ProductVM product2ProductVM(Product product) {
        Long amount = getProductAmountFromCache(product.getId());
        if (amount == null) {
            amount = product.getAmount();
            setProductAmountToCache(product.getId(), amount);
        }
        var ppMap = modelMapper.typeMap(Product.class, ProductVM.ProductVMBuilder.class)
                .addMappings(mapping -> mapping.skip(ProductVM.ProductVMBuilder::tags));
        return ppMap.map(product)
                .tags(product.getTags().stream().map(tagService::tag2String).collect(Collectors.toSet()))
                .amount(amount)
                .imagePaths(product.getImages().stream().map(dbFileService::generateDownloadLink).collect(Collectors.toList()))
                .build();
    }

    //    @Cacheable(cacheNames = "products")
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
