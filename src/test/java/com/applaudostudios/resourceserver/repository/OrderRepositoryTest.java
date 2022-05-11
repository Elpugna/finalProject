package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.Order;
import java.util.List;
import java.util.Optional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("The Order Repository: ")
class OrderRepositoryTest extends BaseContainer {
  @Autowired
  private OrderRepository orderRepository;

  OrderRepositoryTest() {
  }

  @Test
  @DisplayName("Should find orders list by a given email.")
  void shouldFindByEmail() {
    Optional<List<Order>> expectedList = this.orderRepository.findByEmail("jane@doe.com");
    MatcherAssert.assertThat(((List)expectedList.get()).size(), Matchers.is(Matchers.equalTo(3)));
  }

  @Test
  @DisplayName("Should return an Optional<> empty value if there are no orders matching the given email. ")
  void shouldNotFindByBadEmail() {
    Optional<List<Order>> expectedList = this.orderRepository.findByEmail("john@doe.com");
    MatcherAssert.assertThat(((List)expectedList.get()).isEmpty(), Matchers.is(Matchers.equalTo(true)));
  }
}
