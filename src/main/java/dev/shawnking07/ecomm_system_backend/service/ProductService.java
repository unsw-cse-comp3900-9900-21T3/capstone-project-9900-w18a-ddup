package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    /**
     * create new product
     *
     * @param productDTO
     */
    Product addProduct(ProductDTO productDTO);

    /**
     * edit a product
     *
     * @param id         Product id
     * @param productDTO product info
     * @return edited product
     */
    Product editProduct(Long id, ProductDTO productDTO);

    /**
     * delete a product
     *
     * @param id Product id
     */
    void deleteProduct(Long id);

    ProductVM queryProduct(Long id);

    /**
     * Simply return all products
     *
     * @return All Products
     */
    List<ProductVM> listProducts();

    /**
     * List current user's recommended products <br>
     * Automatically get current user's information from SecurityContext
     *
     * @return Products
     */
    List<Product> listProductsWithRecommendation();

    /**
     * Paged {@link ProductService#listProductsWithRecommendation()}
     *
     * @param pageable page head information
     * @return paged products
     */
    Page<Product> listProductsWithRecommendation(Pageable pageable);
}
