package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Customer;
import java.util.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("The Customer Repository: ")
class CustomerRepositoryTest extends BaseContainer {
  @Autowired
  private CustomerRepository customerRepository;

  CustomerRepositoryTest() {
  }

  @Test
  @DisplayName("Should find the customers matching a given email. ")
  void shouldFindByEmail() {
    Optional<Customer> expectedCustomer = this.customerRepository.findByEmail("jane@doe.com");
    MatcherAssert.assertThat(((Customer)expectedCustomer.get()).getEmail(), Matchers.is(Matchers.equalTo("jane@doe.com")));
    MatcherAssert.assertThat(((Customer)expectedCustomer.get()).getAuthServerId(), Matchers.is(Matchers.equalTo("df968d67-5f4f-46b3-b491-9150b215fd13")));
    MatcherAssert.assertThat(((Customer)expectedCustomer.get()).getUserName(), Matchers.is(Matchers.equalTo("janedoe")));
  }

  @Test
  @DisplayName("Should return an Optional<> empty value if there is no customer matching the given email ")
  void shouldFailWhenFindByBadEmail() {
    Optional<Customer> expectedCustomer = this.customerRepository.findByEmail("Margarita@doe.com");
    MatcherAssert.assertThat(expectedCustomer.isEmpty(), Matchers.is(Matchers.equalTo(true)));
  }

  @Test
  @DisplayName("Should find the customers matching a given the Resource server's id. ")
  void shouldFindByAuthServerId() {
    Customer expectedCustomer = this.customerRepository.findByAuthServerId("df968d67-5f4f-46b3-b491-9150b215fd13");
    MatcherAssert.assertThat(expectedCustomer.getEmail(), Matchers.is(Matchers.equalTo("jane@doe.com")));
    MatcherAssert.assertThat(expectedCustomer.getAuthServerId(), Matchers.is(Matchers.equalTo("df968d67-5f4f-46b3-b491-9150b215fd13")));
    MatcherAssert.assertThat(expectedCustomer.getUserName(), Matchers.is(Matchers.equalTo("janedoe")));
  }

  @Test
  @DisplayName("Should return null when passing a non saved Resource server's id. ")
  void shouldNotFindByAuthServerId() {
    Customer expectedCustomer = this.customerRepository.findByAuthServerId("df9dr67-5f4f-46b3-b491-9150b2133333");
    MatcherAssert.assertThat(expectedCustomer, Matchers.is(Matchers.equalTo((Object)null)));
  }
}
