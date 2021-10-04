package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.entity.Tag;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.repository.TagRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final static String productAmountKey = "product.amount.";

    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final DbFileService dbFileService;
    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    public ProductServiceImpl(ProductRepository productRepository, TagRepository tagRepository, ModelMapper modelMapper, DbFileService dbFileService) {
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
        this.dbFileService = dbFileService;
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
    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public Product addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Set<String> tags = productDTO.getTags();

        product.setTags(tags.stream().map(v -> {
            Optional<Tag> byNameContains = tagRepository.findByNameContains(v);
            return byNameContains.orElse(new Tag(v));
        }).collect(Collectors.toSet()));

        List<DbFile> images = multipartFile2DbFile(productDTO.getFiles());
        product.setImages(images);
        productRepository.save(product);
        redisTemplate.opsForValue().set(productAmountKey + product.getId(), product.getAmount());
        return product;
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public Product editProduct(Long id, ProductDTO productDTO) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id dose not exist");
        var product = byId.get();
        modelMapper.map(productDTO, product);
        product.setImages(multipartFile2DbFile(productDTO.getFiles()));
        productRepository.save(product);
        redisTemplate.opsForValue().set(productAmountKey + product.getId(), product.getAmount());
        return product;
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        redisTemplate.delete(productAmountKey + id);
    }

    @Override
    public ProductVM queryProduct(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id does not exist");
        return product2ProductVM(byId.get());
    }

    private ProductVM product2ProductVM(Product product) {
        Long amount = redisTemplate.opsForValue().get(productAmountKey + product.getId());
        return modelMapper.map(product, ProductVM.ProductVMBuilder.class)
                .amount(amount)
                .imagePaths(product.getImages().stream().map(dbFileService::generateDownloadLink).collect(Collectors.toList()))
                .build();
    }

    @Cacheable(cacheNames = "products")
    @Override
    public List<ProductVM> listProducts() {
        return productRepository.findAll().stream()
                .map(this::product2ProductVM)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> listProductsWithRecommendation() {
        // TODO: recommendation
        return null;
    }

    @Override
    public Page<Product> listProductsWithRecommendation(Pageable pageable) {
        return null;
    }
}
