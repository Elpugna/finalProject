package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.Customer;

public interface CustomerService {
  Customer save(Customer customer);

  Customer update(Customer customer);

  Customer findByEmail(String email);

  Customer findByAuthServerId(String oAuth2Id);
}
