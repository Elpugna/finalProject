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
public class CheckoutItemPk implements Serializable {
  private static final long serialVersionUID = -612341231245161871L;
  @Transient
  @ManyToOne(
          optional = false,
          fetch = FetchType.EAGER
  )
  @JoinColumn(
          name = "checkout_id"
  )
  private Checkout checkout;
  @Transient
  @ManyToOne(
          fetch = FetchType.EAGER
  )
  @JoinColumn(
          name = "product_id"
  )
  private Product product;

  public CheckoutItemPk(final Checkout checkout, final Product product) {
    this.checkout = checkout;
    this.product = product;
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.checkout.getId(), "RandomString"});
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof CheckoutItemPk)) {
      return false;
    } else {
      CheckoutItemPk that = (CheckoutItemPk)o;
      return this.getCheckout().getId().equals(that.getCheckout().getId()) && this.getProduct().getId().equals(that.getProduct().getId());
    }
  }

  @Generated
  public Checkout getCheckout() {
    return this.checkout;
  }

  @Generated
  public Product getProduct() {
    return this.product;
  }

  @Generated
  public void setCheckout(final Checkout checkout) {
    this.checkout = checkout;
  }

  @Generated
  public void setProduct(final Product product) {
    this.product = product;
  }

  @Generated
  public CheckoutItemPk() {
  }
}