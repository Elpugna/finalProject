package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.PaymentMethod;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("The Payment Method Repository: ")
class PaymentMethodRepositoryTest extends BaseContainer {
  @Autowired
  private PaymentMethodRepository paymentMethodRepository;
  @Autowired
  private CustomerRepository customerRepository;

  PaymentMethodRepositoryTest() {
  }

  @Test
  @DisplayName("Should find the payment methods from a given customer.")
  void ShouldFindByCustomer() {
    Customer customer = (Customer)this.customerRepository.getById(1L);
    List<PaymentMethod> paymentMethods = this.paymentMethodRepository.findByCustomer(customer);
    MatcherAssert.assertThat(paymentMethods.size(), Matchers.is(Matchers.equalTo(2)));
    MatcherAssert.assertThat(((PaymentMethod)paymentMethods.get(0)).getPaymentMethod(), Matchers.is(Matchers.equalTo("Visa")));
    MatcherAssert.assertThat(((PaymentMethod)paymentMethods.get(1)).getPaymentMethod(), Matchers.is(Matchers.equalTo("Bank transfer")));
  }

  @Test
  @DisplayName("Should return an empty list when customer does not have associated methods.")
  void ShouldNotFindByEmptyCustomer() {
    Customer customer = (Customer)this.customerRepository.getById(3L);
    List<PaymentMethod> paymentMethods = this.paymentMethodRepository.findByCustomer(customer);
    MatcherAssert.assertThat(paymentMethods.isEmpty(), Matchers.is(Matchers.equalTo(true)));
  }
}
