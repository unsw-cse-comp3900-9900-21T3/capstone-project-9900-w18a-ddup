package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.dto.ProductDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductPatchDTO;
import dev.shawnking07.ecomm_system_backend.dto.ProductVM;
import dev.shawnking07.ecomm_system_backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Admin add product",
            description = "There are some issues for OpenAPI generation. <br>" +
                    "'Try it out' may not be working."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVM addProduct(@Valid ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @Operation(summary = "Partial update product info")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductVM editProduct(@PathVariable Long id, @Valid ProductPatchDTO productPatchDTO) {
        return productService.patchProduct(id, productPatchDTO);
    }

    @Operation(summary = "Delete product info")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @Operation(summary = "Query product by id")
    @GetMapping("/{id}")
    public ProductVM queryProduct(@PathVariable Long id) {
        return productService.queryProduct(id);
    }

    @Operation(summary = "Search products", description = "full text search on name and description. <br>If nothing searched, simply return all data")
    @GetMapping
    public List<ProductVM> searchProducts(@RequestParam(required = false) Optional<String> search) {
        return productService.searchProducts(search.orElse(null));
    }
}
