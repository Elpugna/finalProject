package com.applaudostudios.resourceserver.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Generated;

@Entity
@Table(
        name = "orders"
)
public class Order {
  @Id
  @GeneratedValue(
          strategy = GenerationType.IDENTITY
  )
  private Long id;
  private String authServerId;
  private LocalDateTime createdAt;
  private String email;
  private String userName;
  @OneToMany(
          mappedBy = "orderItemPk.order"
  )
  private List<OrderItem> orderItems;
  @NotNull(
          message = "Order's total price must be provided"
  )
  private Double totalPrice;
  @NotBlank(
          message = "The payment must be present"
  )
  private String paymentMethod;
  @Embedded
  private Delivery delivery;

  public Order(@NotBlank(message = "The customer email must be provided") final String email, @NotNull(message = "Order's total price is required") final Double totalPrice, @NotBlank(message = "Order's username is required") final String userName, @NotBlank(message = "Order's authServerId is required") final String authServerId, @NotBlank(message = "Order's paymentMethod is required") final String paymentMethod) {
    this.orderItems = new ArrayList();
    this.email = email;
    this.userName = userName;
    this.totalPrice = totalPrice;
    this.createdAt = LocalDateTime.now();
    this.authServerId = authServerId;
    this.paymentMethod = paymentMethod;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Order) {
      Order that = (Order)o;
      return Objects.equals(this.getId(), that.getId());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.getId()});
  }

  @Generated
  public Long getId() {
    return this.id;
  }

  @Generated
  public String getAuthServerId() {
    return this.authServerId;
  }

  @Generated
  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  @Generated
  public String getEmail() {
    return this.email;
  }

  @Generated
  public String getUserName() {
    return this.userName;
  }

  @Generated
  public List<OrderItem> getOrderItems() {
    return this.orderItems;
  }

  @NotNull(
          message = "Order's total price must be provided"
  )
  @Generated
  public Double getTotalPrice() {
    return this.totalPrice;
  }

  @Generated
  public String getPaymentMethod() {
    return this.paymentMethod;
  }

  @Generated
  public Delivery getDelivery() {
    return this.delivery;
  }

  @Generated
  public void setId(final Long id) {
    this.id = id;
  }

  @Generated
  public void setAuthServerId(final String authServerId) {
    this.authServerId = authServerId;
  }

  @Generated
  public void setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Generated
  public void setEmail(final String email) {
    this.email = email;
  }

  @Generated
  public void setUserName(final String userName) {
    this.userName = userName;
  }

  @Generated
  public void setOrderItems(final List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  @Generated
  public void setTotalPrice(@NotNull(message = "Order's total price must be provided") final Double totalPrice) {
    this.totalPrice = totalPrice;
  }

  @Generated
  public void setPaymentMethod(final String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Generated
  public void setDelivery(final Delivery delivery) {
    this.delivery = delivery;
  }

  @Generated
  public Order() {
  }
}
