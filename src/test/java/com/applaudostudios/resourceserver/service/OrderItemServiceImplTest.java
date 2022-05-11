package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.model.OrderItem;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.OrderItemRepository;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Order Item Service:")
class OrderItemServiceImplTest {
  @Mock
  OrderItemRepository orderItemRepository;
  OrderItemService orderItemService;
  OrderItem orderItem;
  Product product;
  Order order;

  OrderItemServiceImplTest() {
  }

  @BeforeEach
  public void setup() {
    this.orderItemService = new OrderItemServiceImpl(this.orderItemRepository);
    this.product = new Product("Spoon", 2.0D);
    this.product.setId(2L);
    this.order = new Order();
    this.order.setId(1L);
    this.orderItem = new OrderItem(this.order, this.product, 3L);
  }

  @Nested
  @DisplayName("The addItems method:")
  class AddItems {
    List<CheckoutItem> itemList;

    AddItems() {
    }

    @BeforeEach
    public void setupNested() {
      Checkout checkout = new Checkout();
      checkout.setId(1L);
      Product extraProduct = new Product("Onion", 2.0D);
      extraProduct.setId(3L);
      this.itemList = new ArrayList();
      this.itemList.add(new CheckoutItem(checkout, OrderItemServiceImplTest.this.product, 2L));
      this.itemList.add(new CheckoutItem(checkout, extraProduct, 6L));
    }

    @Test
    @DisplayName("Given a set of checkoutItems and a order it must create a set of orderItems")
    void addItems() {
      OrderItemServiceImplTest.this.orderItemService.addItems(this.itemList, OrderItemServiceImplTest.this.order);
      ArgumentCaptor<OrderItem> orderItemArgumentCaptor = ArgumentCaptor.forClass(OrderItem.class);
      ((OrderItemRepository)Mockito.verify(OrderItemServiceImplTest.this.orderItemRepository, Mockito.times(2))).save((OrderItem)orderItemArgumentCaptor.capture());
      List<OrderItem> orderItemsCaptured = orderItemArgumentCaptor.getAllValues();
      MatcherAssert.assertThat(orderItemsCaptured.size(), Matchers.is(Matchers.equalTo(2)));
      MatcherAssert.assertThat(((OrderItem)orderItemsCaptured.get(0)).getQuantity(), Matchers.is(Matchers.equalTo(2L)));
      MatcherAssert.assertThat(((OrderItem)orderItemsCaptured.get(0)).getOrderItemPk().getProduct().getName(), Matchers.is(Matchers.equalTo("Spoon")));
      MatcherAssert.assertThat(((OrderItem)orderItemsCaptured.get(1)).getQuantity(), Matchers.is(Matchers.equalTo(6L)));
      MatcherAssert.assertThat(((OrderItem)orderItemsCaptured.get(1)).getOrderItemPk().getProduct().getName(), Matchers.is(Matchers.equalTo("Onion")));
    }
  }

  @Nested
  @DisplayName("The save method:")
  class Save {
    Save() {
    }

    @Test
    @DisplayName("Should save (persist) a given Order item")
    void save() {
      OrderItemServiceImplTest.this.orderItemService.save(OrderItemServiceImplTest.this.orderItem);
      ArgumentCaptor<OrderItem> orderItemArgumentCaptor = ArgumentCaptor.forClass(OrderItem.class);
      ((OrderItemRepository)Mockito.verify(OrderItemServiceImplTest.this.orderItemRepository)).save((OrderItem)orderItemArgumentCaptor.capture());
      OrderItem orderItemCaptured = (OrderItem)orderItemArgumentCaptor.getValue();
      MatcherAssert.assertThat(orderItemCaptured.getQuantity(), Matchers.is(Matchers.equalTo(3L)));
      MatcherAssert.assertThat(orderItemCaptured.getPrice(), Matchers.is(Matchers.equalTo(2.0D)));
      MatcherAssert.assertThat(orderItemCaptured.getOrderItemPk().getProduct(), Matchers.is(Matchers.equalTo(OrderItemServiceImplTest.this.product)));
      MatcherAssert.assertThat(orderItemCaptured.getOrderItemPk().getOrder(), Matchers.is(Matchers.equalTo(OrderItemServiceImplTest.this.order)));
    }
  }
}
