package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    /**
     * create new product
     */
    Product addProduct();

    /**
     * edit a product
     *
     * @param id Product id
     * @return edited product
     */
    Product editProduct(Long id);

    /**
     * delete a product
     *
     * @param id Product id
     */
    void deleteProduct(Long id);

    /**
     * Simply return all products
     *
     * @return All Products
     */
    List<Product> listProducts();

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
