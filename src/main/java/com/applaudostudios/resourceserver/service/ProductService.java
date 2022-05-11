package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.Product;

public interface ProductService {
  Product save(Product product);

  Product getProduct(Long id);
}
