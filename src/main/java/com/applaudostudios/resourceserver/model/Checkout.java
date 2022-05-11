package com.applaudostudios.resourceserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import lombok.Generated;
import org.hibernate.annotations.NaturalId;

@Entity
public class Checkout {
  @Id
  @GeneratedValue(
          strategy = GenerationType.IDENTITY
  )
  private Long id;
  @NaturalId
  private String email;
  @NotBlank
  private String userName;
  @OneToMany(
          mappedBy = "checkoutItemPk.checkout",
          orphanRemoval = true
  )
  private List<CheckoutItem> checkoutItems;
  @OneToOne
  @JoinColumn(
          name = "address_id",
          referencedColumnName = "id"
  )
  private Address address;
  @OneToOne
  @JoinColumn(
          name = "payment_method_id",
          referencedColumnName = "id"
  )
  private PaymentMethod paymentMethod;

  public Checkout(@NotBlank(message = "The customer email must be provided") final String email, @NotBlank(message = "The customer username must be provided") final String userName) {
    this.checkoutItems = new ArrayList();
    this.email = email;
    this.userName = userName;
    this.address = null;
    this.paymentMethod = null;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Checkout) {
      Checkout that = (Checkout)o;
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
  public String getEmail() {
    return this.email;
  }

  @Generated
  public String getUserName() {
    return this.userName;
  }

  @Generated
  public List<CheckoutItem> getCheckoutItems() {
    return this.checkoutItems;
  }

  @Generated
  public Address getAddress() {
    return this.address;
  }

  @Generated
  public PaymentMethod getPaymentMethod() {
    return this.paymentMethod;
  }

  @Generated
  public void setId(final Long id) {
    this.id = id;
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
  public void setCheckoutItems(final List<CheckoutItem> checkoutItems) {
    this.checkoutItems = checkoutItems;
  }

  @Generated
  public void setAddress(final Address address) {
    this.address = address;
  }

  @Generated
  public void setPaymentMethod(final PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Generated
  public Checkout() {
  }
}
