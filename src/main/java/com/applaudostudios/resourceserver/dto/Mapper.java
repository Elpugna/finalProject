package com.applaudostudios.resourceserver.dto;

import com.applaudostudios.resourceserver.dto.responsedto.CheckoutResDto;
import com.applaudostudios.resourceserver.dto.responsedto.ItemResDto;
import com.applaudostudios.resourceserver.dto.responsedto.OrderResDto;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Mapper {
  private Mapper() {
  }

  public static CheckoutResDto checkoutToCheckoutResDto(final Checkout checkout) {
    CheckoutResDto checkoutResDto = new CheckoutResDto();
    Set<ItemResDto> items = new HashSet();
    Iterator var3 = checkout.getCheckoutItems().iterator();

    while(var3.hasNext()) {
      CheckoutItem i = (CheckoutItem)var3.next();
      items.add(itemToItemResDto(i));
    }

    checkoutResDto.setUserName(checkout.getUserName());
    checkoutResDto.setEmail(checkout.getEmail());
    if (checkout.getAddress() != null) {
      checkoutResDto.setAddress(checkout.getAddress().getAddress());
    }

    if (checkout.getPaymentMethod() != null) {
      checkoutResDto.setPaymentMethod(checkout.getPaymentMethod().getPaymentMethod());
    }

    checkoutResDto.setProducts(items);
    return checkoutResDto;
  }

  public static List<OrderResDto> ordersToOrderRestDtoList(final List<Order> orders) {
    List<OrderResDto> orderResDtoList = new ArrayList();
    Iterator var2 = orders.iterator();

    while(var2.hasNext()) {
      Order o = (Order)var2.next();
      orderResDtoList.add(orderToOrderResDto(o));
    }

    return orderResDtoList;
  }

  private static OrderResDto orderToOrderResDto(final Order order) {
    OrderResDto orderResDto = new OrderResDto();
    Set<ItemResDto> items = new HashSet();
    Iterator var3 = order.getOrderItems().iterator();

    while(var3.hasNext()) {
      OrderItem i = (OrderItem)var3.next();
      items.add(itemToItemResDto(i));
    }

    orderResDto.setUserName(order.getUserName());
    orderResDto.setEmail(order.getEmail());
    orderResDto.setAddress(order.getDelivery().getAddress());
    orderResDto.setPaymentMethod(order.getPaymentMethod());
    orderResDto.setProducts(items);
    orderResDto.setTotalPrice(order.getTotalPrice());
    return orderResDto;
  }

  private static ItemResDto itemToItemResDto(final CheckoutItem checkoutItem) {
    ItemResDto item = new ItemResDto();
    item.setName(checkoutItem.getCheckoutItemPk().getProduct().getName());
    item.setPrice(checkoutItem.getCheckoutItemPk().getProduct().getPrice());
    item.setQuantity(checkoutItem.getQuantity());
    item.setId(checkoutItem.getCheckoutItemPk().getProduct().getId());
    return item;
  }

  private static ItemResDto itemToItemResDto(final OrderItem orderItem) {
    ItemResDto item = new ItemResDto();
    item.setName(orderItem.getOrderItemPk().getProduct().getName());
    item.setPrice(orderItem.getPrice());
    item.setQuantity(orderItem.getQuantity());
    item.setId(orderItem.getOrderItemPk().getProduct().getId());
    return item;
  }
}
