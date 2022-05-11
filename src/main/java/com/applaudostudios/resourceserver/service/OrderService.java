package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.Order;
import java.util.List;

public interface OrderService {
  Order save(Order order);

  Order create(String email);

  List<Order> getOrders(String userEmail);
}
