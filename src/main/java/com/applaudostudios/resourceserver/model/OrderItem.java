package com.applaudostudios.resourceserver.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Generated;

@Entity
public class OrderItem {
  @EmbeddedId
  private OrderItemPk orderItemPk;
  @NotNull(
          message = "Order's item quantity must be provided"
  )
  @Min(
          value = 1L,
          message = "The item quantity must be greater than 0 to place an order"
  )
  @Column(
          nullable = false
  )
  private Long quantity;
  @NotNull(
          message = "Order's item unitary price must be provided"
  )
  @Column(
          nullable = false
  )
  private Double price;

  public OrderItem(final Order order, final Product product, @NotNull(message = "Order item quantity is required") @Min(value = 1L,message = "The quantity must be greater or equal to 1 ") final Long quantity) {
    this.orderItemPk = new OrderItemPk();
    this.orderItemPk.setOrder(order);
    this.orderItemPk.setProduct(product);
    this.quantity = quantity;
    this.price = product.getPrice();
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof OrderItem)) {
      return false;
    } else {
      OrderItem that = (OrderItem)o;
      return Objects.equals(this.orderItemPk.getProduct(), that.getOrderItemPk().getProduct()) && Objects.equals(this.orderItemPk.getOrder(), that.getOrderItemPk().getOrder());
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.orderItemPk.getProduct().getId(), 42});
  }

  @Generated
  public OrderItemPk getOrderItemPk() {
    return this.orderItemPk;
  }

  @NotNull(
          message = "Order's item quantity must be provided"
  )
  @Generated
  public Long getQuantity() {
    return this.quantity;
  }

  @NotNull(
          message = "Order's item unitary price must be provided"
  )
  @Generated
  public Double getPrice() {
    return this.price;
  }

  @Generated
  public void setOrderItemPk(final OrderItemPk orderItemPk) {
    this.orderItemPk = orderItemPk;
  }

  @Generated
  public void setQuantity(@NotNull(message = "Order's item quantity must be provided") final Long quantity) {
    this.quantity = quantity;
  }

  @Generated
  public void setPrice(@NotNull(message = "Order's item unitary price must be provided") final Double price) {
    this.price = price;
  }

  @Generated
  public OrderItem() {
  }
}
