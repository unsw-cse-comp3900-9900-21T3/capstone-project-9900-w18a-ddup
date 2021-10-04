package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.DbFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbFileRepository extends JpaRepository<DbFile, Long> {
}