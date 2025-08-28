package com.soi.sok.wisdom.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.soi.sok.wisdom.data.entity.Product;

import java.util.UUID;
import java.util.Optional;


public interface ProductRepository  extends JpaRepository<Product, UUID>{
  Optional<Product>  findByName(String name);
}
