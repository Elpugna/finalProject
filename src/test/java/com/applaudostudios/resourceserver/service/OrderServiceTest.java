package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.CheckoutIsNotReadyException;
import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Address;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.Delivery;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import com.applaudostudios.resourceserver.model.PaymentMethod;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Order Service:")
class OrderServiceTest {
  @Mock
  OrderRepository orderRepository;
  OrderService orderService;
  Order order;
  @Mock
  OrderItemService orderItemService;
  @Mock
  CheckoutService checkoutService;
  @Mock
  CustomerService customerService;
  

  @BeforeEach
  void setup() {
    orderService = new OrderServiceImpl(
            orderRepository, 
            orderItemService, 
            checkoutService, 
            customerService);
    order = new Order("jane@doe.com", 100.0D, "janedoe", 
            "12345-1234-4321", "Dogecoint");
    order.setId(1L);
  }

  @Nested
  @DisplayName("The getOrders method:")
  class GetOrders {
    @Test
    @DisplayName("Should return a customer's order list for a given email")
    void getOrders() {
      when(orderRepository.findByEmail(anyString())).thenReturn(Optional.of(List.of(order)));
      List<Order> orderListExpected = orderService.getOrders("jane@doe.com");
      verify(orderRepository).findByEmail("jane@doe.com");
      assertThat(orderListExpected.size(), is(equalTo(1)));
      assertThat(orderListExpected.get(0).getId(), equalTo(order.getId()));
      assertThat(orderListExpected.get(0).getTotalPrice(), equalTo(order.getTotalPrice()));
      assertThat(orderListExpected.get(0).getEmail(), equalTo(order.getEmail()));
    }
    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" if the customer has no orders")
    void getEmptyOrderList() {
      when(orderRepository.findByEmail(anyString())).thenReturn(Optional.empty());
      assertThatThrownBy(() -> {
        orderService.getOrders("jane@doe.com");
      }).isInstanceOf(ResourceNotFoundException.class);
      verify(orderRepository).findByEmail("jane@doe.com");
    }
  }
  @Nested
  @DisplayName("The create method:")
  class Create {
    @Test
    @DisplayName("Should return a \"CheckoutIsNotReadyException\" when trying to generate an order with an inclomplete checkout")
    void attemptToCreateOrder() {
      Checkout invalidCheckout = new Checkout("jane@doe.com", "janedoe");
      invalidCheckout.setId(144L);
      invalidCheckout.getCheckoutItems().add(new CheckoutItem());
      when(checkoutService.getUserCheckout(anyString())).thenReturn(invalidCheckout);
      assertThatThrownBy(() -> {
        orderService.create("jane@doe.com");
      }).isInstanceOf(CheckoutIsNotReadyException.class);
      verify(checkoutService).getUserCheckout("jane@doe.com");
    }

    @Test
    @DisplayName("Should create an order with the user email")
    void shouldCreateOrder() {
      Checkout checkout = new Checkout("jane@doe.com", "janedoe");
      checkout.setId(1L);
      Customer customer = new Customer("12345-1234-4321", "jane@doe.com", "janedoe", "Doe", "Jane");
      customer.setId(2L);
      Product product = new Product("spoon", 12.0D);
      Address address = new Address(customer, "the street");
      address.setCheckout(checkout);
      customer.getAddresses().add(address);
      checkout.setAddress(address);
      PaymentMethod paymentMethod = new PaymentMethod(customer, "debt");
      paymentMethod.setCheckout(checkout);
      customer.getPaymentMethods().add(paymentMethod);
      checkout.setPaymentMethod(paymentMethod);
      CheckoutItem checkoutItem = new CheckoutItem(checkout, product, 5L);
      checkout.getCheckoutItems().add(checkoutItem);
      OrderItem item = new OrderItem(order, product, 5L);
      order.setTotalPrice(60.0D);
      order.getOrderItems().add(item);
      Order newOrder = new Order("jane@doe.com", 0.0D, "janedoe", "12345-1234-4321", "Litecoin");
      newOrder.setOrderItems(new ArrayList());
      newOrder.setPaymentMethod(paymentMethod.getPaymentMethod());
      newOrder.setDelivery(new Delivery(address.getAddress()));
      when(checkoutService.getUserCheckout(anyString())).thenReturn(checkout);
      when(customerService.findByEmail(anyString())).thenReturn(customer);
      when(orderRepository.saveAndFlush(ArgumentMatchers.any(Order.class))).thenReturn(newOrder);
      when(orderItemService.addItems(ArgumentMatchers.anyList(), ArgumentMatchers.any(Order.class))).thenReturn(new ArrayList(List.of(item)));
      when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
      orderService.create("jane@doe.com");
      ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
      verify(orderRepository).saveAndFlush(orderArgumentCaptor.capture());
      Order firstOrderCaptured = orderArgumentCaptor.getValue();
      ArgumentCaptor<Order> secondOrderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
      verify(orderRepository).save(secondOrderArgumentCaptor.capture());
      Order secondOrderCaptured = secondOrderArgumentCaptor.getValue();
      verify(checkoutService, Mockito.atMostOnce()).getUserCheckout("jane@doe.com");
      verify(customerService, Mockito.atMostOnce()).findByEmail("jane@doe.com");
      verify(orderItemService, Mockito.atMostOnce()).addItems(checkout.getCheckoutItems(), newOrder);
      verify(orderRepository, Mockito.atMostOnce()).saveAndFlush(ArgumentMatchers.any());
      assertThat(firstOrderCaptured.getEmail(), is(equalTo(newOrder.getEmail())));
      assertThat(firstOrderCaptured.getAuthServerId(), is(equalTo(newOrder.getAuthServerId())));
      assertThat(Objects.equals( firstOrderCaptured, newOrder), is(equalTo(true)));
      assertThat(secondOrderCaptured.getEmail(), is(equalTo("jane@doe.com")));
      assertThat(secondOrderCaptured.getAuthServerId(), is(equalTo("12345-1234-4321")));
      assertThat(secondOrderCaptured.getOrderItems().contains(item), is(equalTo(true)));
    }
  }
  @Nested
  @DisplayName("The save method:")
  class Save {
    @Test
    @DisplayName("Should save (persist) a given Order")
    void save() {
      orderService.save(order);
      ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
      verify(orderRepository).save(orderArgumentCaptor.capture());
      Order orderCaptured = orderArgumentCaptor.getValue();
      assertThat(orderCaptured.getId(), is(equalTo(1L)));
      assertThat(orderCaptured.getTotalPrice(), is(equalTo(100.0D)));
      assertThat(orderCaptured.getEmail(), is(equalTo("jane@doe.com")));
      assertThat(orderCaptured.getAuthServerId(), is(equalTo("12345-1234-4321")));
    }
  }
}
