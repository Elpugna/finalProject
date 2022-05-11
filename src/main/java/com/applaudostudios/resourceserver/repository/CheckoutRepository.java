package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Checkout;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
  Optional<Checkout> findByEmail(String email);
}
