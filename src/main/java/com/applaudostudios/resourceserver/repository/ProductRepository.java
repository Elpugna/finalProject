package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
