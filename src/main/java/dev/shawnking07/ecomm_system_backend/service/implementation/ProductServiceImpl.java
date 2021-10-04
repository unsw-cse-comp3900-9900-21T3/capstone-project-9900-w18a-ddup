package dev.shawnking07.ecomm_system_backend.service.implementation;

import dev.shawnking07.ecomm_system_backend.api.error.ResourceNotFoundException;
import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.repository.ProductRepository;
import dev.shawnking07.ecomm_system_backend.service.DbFileService;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final DbFileService dbFileService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, DbFileService dbFileService) {
        this.productRepository = productRepository;
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

    @Override
    public Product addProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        List<DbFile> images = multipartFile2DbFile(productDTO.getFiles());
        product.setImages(images);
        return productRepository.save(product);
    }

    @Override
    public Product editProduct(Long id, ProductDTO productDTO) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isEmpty()) throw new ResourceNotFoundException("id dose not exist");
        var product = byId.get();
        modelMapper.map(productDTO, product);
        product.setImages(multipartFile2DbFile(productDTO.getFiles()));
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductVM> listProducts() {
        return productRepository.findAll().stream()
                .map(v -> modelMapper.map(v, ProductVM.ProductVMBuilder.class)
                        .imagePaths(v.getImages().stream().map(dbFileService::generateDownloadLink).collect(Collectors.toList()))
                        .build())
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
