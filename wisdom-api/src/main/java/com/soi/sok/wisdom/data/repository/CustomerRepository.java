package com.soi.sok.wisdom.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soi.sok.wisdom.data.entity.Customer;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, UUID>{
  Optional<Customer>  findByEmail(String email);

}
