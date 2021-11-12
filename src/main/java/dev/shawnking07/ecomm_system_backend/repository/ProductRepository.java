package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Product;
import dev.shawnking07.ecomm_system_backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT id,\n" +
            "       created_by,\n" +
            "       created_date,\n" +
            "       last_modified_by,\n" +
            "       last_modified_date,\n" +
            "       amount,\n" +
            "       discount_price,\n" +
            "       price,\n" +
            "       name,\n" +
            "       description\n" +
            "FROM (\n" +
            "         SELECT DISTINCT ts_rank_cd(to_tsvector('english',\n" +
            "                                                p.name || ' ' || coalesce(p.description, '') || ' ' ||\n" +
            "                                                coalesce(t.name, '')),\n" +
            "                                    plainto_tsquery(?1)) AS score,\n" +
            "                         p.*\n" +
            "         FROM product p\n" +
            "                  LEFT JOIN products_tags pt on p.id = pt.product_id\n" +
            "                  LEFT JOIN tag t on t.id = pt.tag_id\n" +
            "     ) as sc\n" +
            "ORDER BY score DESC", nativeQuery = true)
    List<Product> fullTextSearch(String info);

    @Query(value = "SELECT p.*\n" +
            "FROM product p\n" +
            "LEFT JOIN order_products op on p.id = op.product_id\n" +
            "LEFT JOIN \"order\" o on op.order_id = o.id\n" +
            "WHERE p.amount > 0\n" +
            "GROUP BY p.id\n" +
            "ORDER BY sum(op.amount) DESC NULLS LAST", nativeQuery = true)
    List<Product> findAllProductsOrderBySell();

    List<Product> findDistinctByTagsIn(List<Tag> tags);

    List<Product> findAllByAmountGreaterThan(Long expectedAmount);
}