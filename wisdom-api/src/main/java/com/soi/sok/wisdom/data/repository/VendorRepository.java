package com.soi.sok.wisdom.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soi.sok.wisdom.data.entity.Vendor;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
  Optional<Vendor> findByEmail(String email);

}
