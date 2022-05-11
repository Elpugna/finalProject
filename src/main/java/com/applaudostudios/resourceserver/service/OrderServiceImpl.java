package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.CheckoutIsNotReadyException;
import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.Delivery;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import com.applaudostudios.resourceserver.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemService orderItemService;
  private final CheckoutService checkoutService;
  private final CustomerService customerService;

  public OrderServiceImpl(final OrderRepository orderRepository, final OrderItemService orderItemService, final CheckoutService checkoutService, final CustomerService customerService) {
    this.customerService = customerService;
    this.checkoutService = checkoutService;
    this.orderItemService = orderItemService;
    this.orderRepository = orderRepository;
  }

  public Order save(final Order order) {
    return (Order)this.orderRepository.save(order);
  }

  public Order create(final String email) {
    Checkout checkout = this.checkoutService.getUserCheckout(email);
    if (!this.isCheckoutIsReady(checkout)) {
      String var10002 = checkout.getPaymentMethod() == null ? "*Payment method* " : "";
      throw new CheckoutIsNotReadyException("You have to first add: " + var10002 + (checkout.getAddress() == null ? "*Address*" : ""));
    } else {
      Customer customer = this.customerService.findByEmail(email);
      Order order = new Order();
      order.setOrderItems(new ArrayList());
      order.setEmail(email);
      order.setTotalPrice(0.0D);
      order.setAuthServerId(customer.getAuthServerId());
      order.setPaymentMethod(checkout.getPaymentMethod().getPaymentMethod());
      Delivery delivery = new Delivery(checkout.getAddress().getAddress());
      order.setDelivery(delivery);
      order.setUserName(customer.getUserName());
      order = (Order)this.orderRepository.saveAndFlush(order);
      List<OrderItem> orderItems = this.orderItemService.addItems(checkout.getCheckoutItems(), order);
      order.setOrderItems(orderItems);
      order.setTotalPrice(this.getOrderTotalPrice(orderItems));
      this.orderRepository.flush();
      order.setCreatedAt(LocalDateTime.now());
      this.checkoutService.removeCheckout(checkout);
      return this.save(order);
    }
  }

  public List<Order> getOrders(final String userEmail) {
    return (List)this.orderRepository.findByEmail(userEmail).orElseThrow(() -> {
      return new ResourceNotFoundException("The user does not has any order associated", "getOrders() - Order");
    });
  }

  private Double getOrderTotalPrice(final List<OrderItem> orderItems) {
    Double price = 0.0D;

    OrderItem i;
    for(Iterator var3 = orderItems.iterator(); var3.hasNext(); price = price + i.getPrice()) {
      i = (OrderItem)var3.next();
    }
    return price;
  }

  private boolean isCheckoutIsReady(final Checkout checkout) {
    return checkout.getPaymentMethod() != null && checkout.getAddress() != null;
  }
}
