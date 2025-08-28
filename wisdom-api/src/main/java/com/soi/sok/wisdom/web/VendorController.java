package com.soi.sok.wisdom.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soi.sok.wisdom.data.repository.VendorRepository;
import com.soi.sok.wisdom.util.exception.NotFoundException;
import com.soi.sok.wisdom.data.entity.Vendor;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("vendors")
@Slf4j
public class VendorController {
  private final VendorRepository vendorRepository;
  public VendorController(VendorRepository vendorRepository){
    this.vendorRepository = vendorRepository;
  }

  @GetMapping
  public Iterable<Vendor> getAllVendors(@RequestParam(required=false) String email) {
    if (StringUtils.hasLength(email)){
      List<Vendor> vendors = new ArrayList<>();
      Optional<Vendor> vendor = this.vendorRepository.findByEmail(email);
      if(vendor.isPresent()){
        vendors.add(vendor.get());
      }
      return vendors;

    }
      return this.vendorRepository.findAll();
  }
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Vendor creaVendor(@RequestBody Vendor vendor) {
    return this.vendorRepository.save(vendor);
  }

  @GetMapping("/{vendorId}")
  public Vendor gVendor(@PathVariable UUID vendorId) {
    Optional<Vendor> vendor = this.vendorRepository.findById(vendorId);
      if(!vendor.isPresent()){
         throw new NotFoundException("Vendor not found with id: "+vendorId);
      }
    return vendor.get();
  }
  
  @PutMapping("/{vendorId}")
  public Vendor putMethodName(@PathVariable UUID vendorId, @RequestBody Vendor vendor) {
    if(vendorId.equals(vendor.getVendorId())){
      throw new NotFoundException("vendorId on path must match body");
    }
    return this.vendorRepository.save(vendor);
  }
  
  @DeleteMapping("/{vendorId}")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public void deleteVendor(@PathVariable UUID vendorId){
    this.vendorRepository.deleteById(vendorId);
  }

}
