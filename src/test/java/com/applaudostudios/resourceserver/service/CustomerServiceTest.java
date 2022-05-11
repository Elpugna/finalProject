package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.repository.CustomerRepository;
import java.util.Objects;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Customer Service:")
class CustomerServiceTest {
  @Mock
  CustomerRepository customerRepository;
  CustomerService customerService;
  Customer customer;

  CustomerServiceTest() {
  }

  @BeforeEach
  public void setUp() {
    this.customerService = new CustomerServiceImpl(this.customerRepository);
    this.customer = new Customer("1234-4321-123", "jane@doe.com", "janedoe", "Doe", "Jane");
    this.customer.setId(1L);
  }

  @Nested
  @DisplayName("The update method:")
  class Update {
    Update() {
    }

    @Test
    @DisplayName("Should update the customer info that came from the Auth server")
    void update() {
      Customer otherCustomer = new Customer("1234-4321-123", "john@doe.com", "johndoe", "Doe", "John");
      Mockito.when(CustomerServiceTest.this.customerRepository.findByAuthServerId(ArgumentMatchers.anyString())).thenReturn(CustomerServiceTest.this.customer);
      ArgumentCaptor<Customer> capturedCustomer = ArgumentCaptor.forClass(Customer.class);
      CustomerServiceTest.this.customerService.update(otherCustomer);
      ((CustomerRepository)Mockito.verify(CustomerServiceTest.this.customerRepository)).save((Customer)capturedCustomer.capture());
      Customer actualCustomer = (Customer)capturedCustomer.getValue();
      MatcherAssert.assertThat(actualCustomer.getEmail(), Matchers.is(Matchers.equalTo("john@doe.com")));
      MatcherAssert.assertThat(actualCustomer.getUserName(), Matchers.is(Matchers.equalTo("johndoe")));
      MatcherAssert.assertThat(actualCustomer.getAuthServerId(), Matchers.is(Matchers.equalTo(CustomerServiceTest.this.customer.getAuthServerId())));
      MatcherAssert.assertThat(Objects.equals(actualCustomer, CustomerServiceTest.this.customer), Matchers.is(true));
    }
  }

  @Nested
  @DisplayName("The findByAuthServerId method:")
  class FindByAuthServerId {
    FindByAuthServerId() {
    }

    @Test
    @DisplayName("Should return the customer with the given AuthServer id")
    void findByAuthServerId() {
      Mockito.when(CustomerServiceTest.this.customerRepository.findByAuthServerId(ArgumentMatchers.anyString())).thenReturn(CustomerServiceTest.this.customer);
      Customer actualCustomer = CustomerServiceTest.this.customerService.findByAuthServerId("1234-4321-123");
      ((CustomerRepository)Mockito.verify(CustomerServiceTest.this.customerRepository)).findByAuthServerId("1234-4321-123");
      MatcherAssert.assertThat(Objects.equals(actualCustomer, CustomerServiceTest.this.customer), Matchers.is(true));
      MatcherAssert.assertThat(actualCustomer.getAuthServerId(), Matchers.is(Matchers.equalTo(CustomerServiceTest.this.customer.getAuthServerId())));
      MatcherAssert.assertThat(actualCustomer.getEmail(), Matchers.is(Matchers.equalTo(CustomerServiceTest.this.customer.getEmail())));
    }

    @Test
    @DisplayName("Should return null when there is no user registered with given AuthServer Id")
    void NotFindByAuthServerId() {
      Mockito.when(CustomerServiceTest.this.customerRepository.findByAuthServerId(ArgumentMatchers.anyString())).thenReturn(null);
      Customer customer = CustomerServiceTest.this.customerService.findByAuthServerId("1234-4321-123");
      ((CustomerRepository)Mockito.verify(CustomerServiceTest.this.customerRepository)).findByAuthServerId("1234-4321-123");
      MatcherAssert.assertThat(customer, Matchers.is(Matchers.equalTo(null)));
    }
  }

  @Nested
  @DisplayName("The findByEmail method:")
  class FindByEmail {
    FindByEmail() {
    }

    @Test
    @DisplayName("Should return the customer with the given email")
    void findByEmail() {
      Mockito.when(CustomerServiceTest.this.customerRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CustomerServiceTest.this.customer));
      Customer actualCustomer = CustomerServiceTest.this.customerService.findByEmail("jane@doe.com");
      ((CustomerRepository)Mockito.verify(CustomerServiceTest.this.customerRepository)).findByEmail("jane@doe.com");
      MatcherAssert.assertThat(Objects.equals(actualCustomer, CustomerServiceTest.this.customer), Matchers.is(Matchers.equalTo(true)));
      MatcherAssert.assertThat(actualCustomer.getAuthServerId(), Matchers.is(Matchers.equalTo(CustomerServiceTest.this.customer.getAuthServerId())));
      MatcherAssert.assertThat(actualCustomer.getEmail(), Matchers.is(Matchers.equalTo(CustomerServiceTest.this.customer.getEmail())));
    }

    @Test
    @DisplayName("hould throw a \"ResourceNotFoundException\" when there is no user registered with given email")
    void NotFindByEmail() {
      Mockito.when(CustomerServiceTest.this.customerRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
      AssertionsForClassTypes.assertThatThrownBy(() -> {
        CustomerServiceTest.this.customerService.findByEmail("jane@doe.com");
      }).isInstanceOf(ResourceNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("The save method:")
  class Save {
    Save() {
    }

    @Test
    @DisplayName("Should save (persist) a given Customer")
    void save() {
      Mockito.when((Customer)CustomerServiceTest.this.customerRepository.save((Customer)ArgumentMatchers.any(Customer.class))).thenReturn(CustomerServiceTest.this.customer);
      Customer actual = CustomerServiceTest.this.customerService.save(CustomerServiceTest.this.customer);
      ((CustomerRepository)Mockito.verify(CustomerServiceTest.this.customerRepository)).save(CustomerServiceTest.this.customer);
      MatcherAssert.assertThat(CustomerServiceTest.this.customer, Matchers.is(Matchers.equalTo(actual)));
      MatcherAssert.assertThat(CustomerServiceTest.this.customer.getId(), Matchers.is(Matchers.equalTo(actual.getId())));
      MatcherAssert.assertThat(CustomerServiceTest.this.customer.getUserName(), Matchers.is(Matchers.equalTo(actual.getUserName())));
    }
  }
}
