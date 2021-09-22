package dev.shawnking07.ecomm_system_backend.entity;

import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity<U, PK extends Serializable> extends AbstractAuditable<U, PK> {
}
