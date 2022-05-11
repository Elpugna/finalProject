package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import com.applaudostudios.resourceserver.repository.OrderItemRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {
  private final OrderItemRepository orderItemRepository;

  @Autowired
  public OrderItemServiceImpl(final OrderItemRepository orderItemRepository) {
    this.orderItemRepository = orderItemRepository;
  }

  public OrderItem save(final OrderItem orderItem) {
    return (OrderItem)this.orderItemRepository.save(orderItem);
  }

  public List<OrderItem> addItems(final List<CheckoutItem> checkoutItems, final Order order) {
    ArrayList<OrderItem> orderItems = new ArrayList();
    Iterator var4 = checkoutItems.iterator();

    while(var4.hasNext()) {
      CheckoutItem i = (CheckoutItem)var4.next();
      OrderItem item = new OrderItem(order, i.getCheckoutItemPk().getProduct(), i.getQuantity());
      this.save(item);
      orderItems.add(item);
    }

    return orderItems;
  }
}
