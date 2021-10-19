package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer_UsernameOrderByIdDesc(String username);

    List<Order> findByPayer_UsernameOrderByIdDesc(String username);

    @Query("from Order o left join o.products p where p.product.createdBy = ?1 order by o.id desc")
    List<Order> findContainProductsCreatedBy(String username);
}