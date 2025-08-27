package com.soi.sok.wisdom.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soi.sok.wisdom.data.repository.CustomerRepository;
import com.soi.sok.wisdom.data.entity.Customer;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("customers")
@Slf4j
public class CustomerController {
  private final CustomerRepository customerRepository;
  public CustomerController(CustomerRepository customerRepository){
    this.customerRepository = customerRepository;
  }

  @GetMapping
  public Iterable<Customer> getAllCustomers(@RequestParam(required=false) String email) {
    if (StringUtils.hasLength(email)){
      List<Customer> customers = new ArrayList<>();
      Optional<Customer> customer = this.customerRepository.findByEmail(email);
      if(customer.isPresent()){
        customers.add(customer.get());
      }
      return customers;

    }
      return this.customerRepository.findAll();
  }
  


}
