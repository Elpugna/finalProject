package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import java.util.List;

public interface OrderItemService {
  OrderItem save(OrderItem orderItem);

  List<OrderItem> addItems(List<CheckoutItem> checkoutItems, Order order);
}
