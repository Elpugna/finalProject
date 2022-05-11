package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<List<Order>> findByEmail(String email);
}
