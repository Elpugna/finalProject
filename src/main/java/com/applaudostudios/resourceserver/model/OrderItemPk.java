package com.applaudostudios.resourceserver.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Generated;
import org.springframework.data.annotation.Transient;

@Embeddable
public class OrderItemPk implements Serializable {
  private static final long serialVersionUID = -612341231978461871L;
  @Transient
  @ManyToOne(
          optional = false,
          fetch = FetchType.EAGER
  )
  @JoinColumn(
          name = "product_id"
  )
  private Product product;
  @Transient
  @ManyToOne(
          optional = false,
          fetch = FetchType.EAGER
  )
  @JoinColumn(
          name = "order_id"
  )
  private Order order;

  public OrderItemPk(final Product product, final Order order) {
    this.product = product;
    this.order = order;
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.order.getId(), "RandomString"});
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof OrderItemPk)) {
      return false;
    } else {
      OrderItemPk that = (OrderItemPk)o;
      return this.getOrder().getId().equals(that.getOrder().getId()) && this.getProduct().getId().equals(that.getProduct().getId());
    }
  }

  @Generated
  public Product getProduct() {
    return this.product;
  }

  @Generated
  public Order getOrder() {
    return this.order;
  }

  @Generated
  public void setProduct(final Product product) {
    this.product = product;
  }

  @Generated
  public void setOrder(final Order order) {
    this.order = order;
  }

  @Generated
  public OrderItemPk() {
  }
}
