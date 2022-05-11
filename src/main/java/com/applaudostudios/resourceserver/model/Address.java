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
public class Address {
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
          mappedBy = "address"
  )
  private Checkout checkout;
  @NotBlank
  private String address;

  public Address(@NotNull(message = "The customer must be provided") final Customer customer, @NotBlank(message = "The address must be provided") final String address) {
    this.customer = customer;
    this.address = address;
    this.checkout = null;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Address)) {
      return false;
    } else {
      Address that = (Address)o;
      return this.address.equals(that.address) && this.customer.getEmail().equals(that.customer.getEmail());
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.address, this.customer.getEmail(), 42});
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
  public String getAddress() {
    return this.address;
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
  public void setAddress(final String address) {
    this.address = address;
  }

  @Generated
  public Address() {
  }
}
