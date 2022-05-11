package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.PaymentMethod;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
  List<PaymentMethod> findByCustomer(Customer customer);
}
