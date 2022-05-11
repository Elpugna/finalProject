package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.repository.CustomerRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerServiceImpl(final CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer save(final Customer customer) {
    return (Customer)this.customerRepository.save(customer);
  }

  public Customer update(final Customer customer) {
    Customer customerToEdit = this.findByAuthServerId(customer.getAuthServerId());
    customerToEdit.setEmail(customer.getEmail());
    customerToEdit.setUserName(customer.getUserName());
    customerToEdit.setLastName(customer.getLastName());
    customerToEdit.setFirstName(customer.getFirstName());
    customerToEdit.setLastLogin(LocalDateTime.now());
    return this.save(customerToEdit);
  }

  public Customer findByEmail(final String email) {
    return (Customer)this.customerRepository.findByEmail(email).orElseThrow(() -> {
      return new ResourceNotFoundException("The user with email " + email + "is not registered", "findByEmail() - CustomerS");
    });
  }

  public Customer findByAuthServerId(final String authServerId) {
    return this.customerRepository.findByAuthServerId(authServerId);
  }
}
