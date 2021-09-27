package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}