package com.applaudostudios.resourceserver.model;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Generated;

@Entity
public class PaymentMethod {
  @Id
  @GeneratedValue(
          strategy = GenerationType.IDENTITY
  )
  private Long id;
  @ManyToOne(
          fetch = FetchType.EAGER
  )
  @JoinColumn(
          name = "customer_id"
  )
  private Customer customer;
  @OneToOne(
          mappedBy = "paymentMethod"
  )
  private Checkout checkout;
  @NotBlank
  private String paymentMethod;

  public PaymentMethod(@NotNull(message = "The customer must be provided") final Customer customer, @NotBlank(message = "The payment method mus be provided") final String paymentMethod) {
    this.customer = customer;
    this.paymentMethod = paymentMethod;
    this.checkout = null;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof PaymentMethod)) {
      return false;
    } else {
      PaymentMethod that = (PaymentMethod)o;
      return this.paymentMethod.equals(that.paymentMethod) && this.customer.getEmail().equals(that.customer.getEmail());
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.paymentMethod, this.id, 42});
  }

  @Generated
  public Long getId() {
    return this.id;
  }

  @Generated
  public Customer getCustomer() {
    return this.customer;
  }

  @Generated
  public Checkout getCheckout() {
    return this.checkout;
  }

  @Generated
  public String getPaymentMethod() {
    return this.paymentMethod;
  }

  @Generated
  public void setId(final Long id) {
    this.id = id;
  }

  @Generated
  public void setCustomer(final Customer customer) {
    this.customer = customer;
  }

  @Generated
  public void setCheckout(final Checkout checkout) {
    this.checkout = checkout;
  }

  @Generated
  public void setPaymentMethod(final String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Generated
  public PaymentMethod() {
  }
}
