package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Autowired
  public ProductServiceImpl(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product save(final Product product) {
    return (Product)this.productRepository.save(product);
  }

  public Product getProduct(final Long id) {
    return (Product)this.productRepository.findById(id).orElseThrow(() -> {
      return new ResourceNotFoundException("Product with id: " + id + "not found", "getProduct() - Product");
    });
  }
}
