package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT DISTINCT p.*\n" +
            "FROM product p\n" +
            "LEFT JOIN products_tags pt on p.id = pt.product_id\n" +
            "LEFT JOIN tag t on t.id = pt.tag_id\n" +
            "WHERE\n" +
            "to_tsvector(p.name || coalesce(p.description,'') || coalesce(t.name, '')) @@ plainto_tsquery(?1)", nativeQuery = true)
    List<Product> fullTextSearch(String info);
}