package dev.shawnking07.ecomm_system_backend.repository;

import dev.shawnking07.ecomm_system_backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}