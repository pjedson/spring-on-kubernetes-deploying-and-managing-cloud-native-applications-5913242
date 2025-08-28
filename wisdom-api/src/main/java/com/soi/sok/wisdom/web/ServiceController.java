package com.soi.sok.wisdom.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soi.sok.wisdom.data.repository.ServiceRepository;
import com.soi.sok.wisdom.util.exception.NotFoundException;
import com.soi.sok.wisdom.data.entity.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
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
@RequestMapping("services")
@Slf4j
public class ServiceController {
  private final ServiceRepository serviceRepository;

  public ServiceController(ServiceRepository serviceRepository) {
    this.serviceRepository = serviceRepository;
  }

  @GetMapping
  public Iterable<Service> getAllServices(@RequestParam(required = false) String name) {
    if (StringUtils.hasLength(name)) {
      List<Service> services = new ArrayList<>();
      Optional<Service> service = this.serviceRepository.findByName(name);
      if (service.isPresent()) {
        services.add(service.get());
      }
      return services;

    }
    return this.serviceRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Service creaService(@RequestBody Service service) {
    return this.serviceRepository.save(service);
  }

  @GetMapping("/{serviceId}")
  public Service gService(@PathVariable UUID serviceId) {
    Optional<Service> service = this.serviceRepository.findById(serviceId);
    if (!service.isPresent()) {
      throw new NotFoundException("Service not found with id: " + serviceId);
    }
    return service.get();
  }

  @PutMapping("/{serviceId}")
  public Service putMethodName(@PathVariable UUID serviceId, @RequestBody Service service) {
    if (serviceId.equals(service.getServiceId())) {
      throw new NotFoundException("serviceId on path must match body");
    }
    return this.serviceRepository.save(service);
  }

  @DeleteMapping("/{serviceId}")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public void deleteService(@PathVariable UUID serviceId) {
    this.serviceRepository.deleteById(serviceId);
  }

}
