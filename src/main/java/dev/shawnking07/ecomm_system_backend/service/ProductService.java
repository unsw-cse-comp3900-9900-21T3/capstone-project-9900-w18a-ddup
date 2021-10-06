package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nullable;
import java.util.List;

public interface ProductService {
    /**
     * create new product
     *
     * @param productDTO
     * @return
     */
    ProductVM addProduct(ProductDTO productDTO);

    /**
     * edit a product
     *
     * @param id         Product id
     * @param productDTO product info
     * @return edited product
     */
    ProductVM editProduct(Long id, ProductDTO productDTO);

    /**
     * delete a product
     *
     * @param id Product id
     */
    void deleteProduct(Long id);

    ProductVM queryProduct(Long id);

    void setProductAmountToCache(Long id, Long amount);

    @Nullable
    Long getProductAmountFromCache(Long id);

    Long getProductAmountFromCache(Long id, Long amount);

    Long decreaseProductAmountInCache(Long id, long delta);

    /**
     * Schedule job for correct the product amount if some orders are expired
     */
    void correctProductAmountInCache();

    ProductVM product2ProductVM(Product product);

    /**
     * Simply return all products <br> cache disabled
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
    List<ProductVM> listProductsWithRecommendation();

    /**
     * Paged {@link ProductService#listProductsWithRecommendation()}
     *
     * @param pageable page head information
     * @return paged products
     */
    Page<ProductVM> listProductsWithRecommendation(Pageable pageable);
}
