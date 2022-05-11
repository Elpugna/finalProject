package com.applaudostudios.resourceserver.configuration.security;

import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.service.CustomerService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MyAuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
  @Generated
  private static final Logger log = LoggerFactory.getLogger(MyAuthenticationSuccessListener.class);
  private final CustomerService customerService;

  @Transactional
  public void onApplicationEvent(final AuthenticationSuccessEvent event) {
    Authentication authentication = event.getAuthentication();
    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Customer"))) {
      String authServerId = ((Jwt)authentication.getPrincipal()).getClaimAsString("sub");
      String email = ((Jwt)authentication.getPrincipal()).getClaimAsString("email");
      String userName = ((Jwt)authentication.getPrincipal()).getClaimAsString("preferred_username");
      String firstName = ((Jwt)authentication.getPrincipal()).getClaimAsString("given_name");
      String lastName = ((Jwt)authentication.getPrincipal()).getClaimAsString("family_name");
      Customer customer = this.customerService.findByAuthServerId(authServerId);
      if (customer == null) {
        if (log.isDebugEnabled()) {
          log.debug("Registering the customer");
        }

        customer = new Customer(authServerId, email, userName, lastName, firstName);
        this.customerService.save(customer);
      } else {
        if (log.isDebugEnabled()) {
          log.debug("Updating the customer");
        }

        Customer customerToUpdate = new Customer(authServerId, email, userName, lastName, firstName);
        this.customerService.update(customerToUpdate);
      }
    }

  }

  @Generated
  public MyAuthenticationSuccessListener(final CustomerService customerService) {
    this.customerService = customerService;
  }
}