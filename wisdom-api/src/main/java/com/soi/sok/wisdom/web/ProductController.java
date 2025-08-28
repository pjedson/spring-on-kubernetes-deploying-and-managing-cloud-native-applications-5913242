package com.soi.sok.wisdom.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soi.sok.wisdom.data.repository.ProductRepository;
import com.soi.sok.wisdom.util.exception.NotFoundException;
import com.soi.sok.wisdom.data.entity.Product;

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
@RequestMapping("products")
@Slf4j
public class ProductController {
  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping
  public Iterable<Product> getAllProducts(@RequestParam(required = false) String name) {
    if (StringUtils.hasLength(name)) {
      List<Product> products = new ArrayList<>();
      Optional<Product> product = this.productRepository.findByName(name);
      if (product.isPresent()) {
        products.add(product.get());
      }
      return products;

    }
    return this.productRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product creaProduct(@RequestBody Product product) {
    return this.productRepository.save(product);
  }

  @GetMapping("/{productId}")
  public Product gProduct(@PathVariable UUID productId) {
    Optional<Product> product = this.productRepository.findById(productId);
    if (!product.isPresent()) {
      throw new NotFoundException("Product not found with id: " + productId);
    }
    return product.get();
  }

  @PutMapping("/{productId}")
  public Product putMethodName(@PathVariable UUID productId, @RequestBody Product product) {
    if (productId.equals(product.getProductId())) {
      throw new NotFoundException("productId on path must match body");
    }
    return this.productRepository.save(product);
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public void deleteProduct(@PathVariable UUID productId) {
    this.productRepository.deleteById(productId);
  }

}
