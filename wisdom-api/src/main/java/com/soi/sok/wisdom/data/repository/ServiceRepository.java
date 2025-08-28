package com.soi.sok.wisdom.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.soi.sok.wisdom.data.entity.Service;

import java.util.UUID;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
  Optional<Service> findByName(String name);
}
