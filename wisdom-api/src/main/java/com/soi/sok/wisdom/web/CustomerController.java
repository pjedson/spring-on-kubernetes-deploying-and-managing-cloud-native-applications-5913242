package com.soi.sok.wisdom.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soi.sok.wisdom.data.repository.CustomerRepository;
import com.soi.sok.wisdom.util.exception.NotFoundException;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import com.soi.sok.wisdom.data.entity.Customer;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("customers")
@Slf4j
public class CustomerController {
  private final CustomerRepository customerRepository;
  private final Map<String, Timer> timerMap;
  private static final String GET_ALL_CUSTOMERS = "getAllCustomers";
  private static final String ADD_CUSTOMER = "addCustomer";
  private static final String GET_CUSTOMER = "getCustomer";
  private static final String PUT_CUSTOMERS = "putCustomer";
  private static final String DELTE_CUSTOMER = "deleteCustomer";

  public CustomerController(CustomerRepository customerRepository, MeterRegistry registry) {
    super();
    this.customerRepository = customerRepository;
    timerMap = new HashMap<>();
    timerMap.put(GET_ALL_CUSTOMERS, registry.timer(GET_ALL_CUSTOMERS));
    timerMap.put(ADD_CUSTOMER, registry.timer(ADD_CUSTOMER));
    timerMap.put(GET_CUSTOMER, registry.timer(GET_CUSTOMER));
    timerMap.put(PUT_CUSTOMERS, registry.timer(PUT_CUSTOMERS));
    timerMap.put(DELTE_CUSTOMER, registry.timer(DELTE_CUSTOMER));
  }

  @GetMapping
  public Iterable<Customer> getAllCustomers(@RequestParam(required = false) String email) {
    Timer.Sample timer = Timer.start();
    if (StringUtils.hasLength(email)) {
      List<Customer> customers = new ArrayList<>();
      Optional<Customer> customer = this.customerRepository.findByEmail(email);
      if (customer.isPresent()) {
        customers.add(customer.get());
      }
      timerMap.get(GET_ALL_CUSTOMERS).record(() -> timer.stop(timerMap.get(GET_ALL_CUSTOMERS)));
      return customers;

    }

    List<Customer> customers = this.customerRepository.findAll();
    timerMap.get(GET_ALL_CUSTOMERS).record(() -> timer.stop(timerMap.get(GET_ALL_CUSTOMERS)));
    return customers;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Customer creaCustomer(@RequestBody Customer customer) {
    return this.customerRepository.save(customer);
  }

  @GetMapping("/{customerId}")
  public Customer gCustomer(@PathVariable UUID customerId) {
    Optional<Customer> customer = this.customerRepository.findById(customerId);
    if (!customer.isPresent()) {
      throw new NotFoundException("Customer not found with id: " + customerId);
    }
    return customer.get();
  }

  @PutMapping("/{customerId}")
  public Customer putCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
    if (customerId.equals(customer.getCustomerId())) {
      throw new NotFoundException("customerId on path must match body");
    }
    return this.customerRepository.save(customer);
  }

  @DeleteMapping("/{customerId}")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public void deleteCustomer(@PathVariable UUID customerId) {
    this.customerRepository.deleteById(customerId);
  }

}
