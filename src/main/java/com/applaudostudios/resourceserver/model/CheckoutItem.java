package com.applaudostudios.resourceserver.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Generated;

@Entity
public class CheckoutItem {
  @EmbeddedId
  private CheckoutItemPk checkoutItemPk;
  @NotNull(
          message = "Checkout item quantity must be provided"
  )
  @Min(1L)
  @Column(
          nullable = false
  )
  private Long quantity;

  public CheckoutItem(final Checkout checkout, final Product product, @NotNull(message = "Checkout item quantity is required") @Min(value = 1L,message = "The quantity must be >= 1 ") final Long quantity) {
    this.checkoutItemPk = new CheckoutItemPk();
    this.checkoutItemPk.setCheckout(checkout);
    this.checkoutItemPk.setProduct(product);
    this.quantity = quantity;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof CheckoutItem)) {
      return false;
    } else {
      CheckoutItem that = (CheckoutItem)o;
      return Objects.equals(this.checkoutItemPk.getProduct(), that.getCheckoutItemPk().getProduct()) && Objects.equals(this.checkoutItemPk.getCheckout(), that.getCheckoutItemPk().getCheckout());
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.checkoutItemPk.getProduct().getId(), 42});
  }

  @Generated
  public CheckoutItemPk getCheckoutItemPk() {
    return this.checkoutItemPk;
  }

  @NotNull(
          message = "Checkout item quantity must be provided"
  )
  @Generated
  public Long getQuantity() {
    return this.quantity;
  }

  @Generated
  public void setCheckoutItemPk(final CheckoutItemPk checkoutItemPk) {
    this.checkoutItemPk = checkoutItemPk;
  }

  @Generated
  public void setQuantity(@NotNull(message = "Checkout item quantity must be provided") final Long quantity) {
    this.quantity = quantity;
  }

  @Generated
  public CheckoutItem() {
  }
}
