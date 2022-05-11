package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Address;
import com.applaudostudios.resourceserver.model.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByCustomer(Customer customer);
}
