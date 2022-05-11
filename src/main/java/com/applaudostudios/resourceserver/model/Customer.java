package com.applaudostudios.resourceserver.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Generated;
import org.hibernate.annotations.NaturalId;

@Entity
public class Customer {
  @Id
  @GeneratedValue(
          strategy = GenerationType.IDENTITY
  )
  private Long id;
  private String userName;
  private String firstName;
  private String lastName;
  @NotBlank(
          message = "The Authorization Server's id must be provided"
  )
  private String authServerId;
  @NaturalId
  @Email(
          message = "The email must be provided"
  )
  private String email;
  @OneToMany(
          mappedBy = "customer"
  )
  private List<Address> addresses;
  @OneToMany(
          mappedBy = "customer"
  )
  private List<PaymentMethod> paymentMethods;
  private LocalDateTime lastLogin;

  public Customer(@NotBlank(message = "The customer's external id must be provided") final String authServerId, @Email(message = "The customer's email must be provided") final String email, @NotBlank(message = "The customer's username must be provided") final String userName, final String lastName, final String firstName) {
    this.userName = userName;
    this.authServerId = authServerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.lastLogin = LocalDateTime.now();
    this.addresses = new ArrayList();
    this.paymentMethods = new ArrayList();
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Customer) {
      Customer that = (Customer)o;
      return Objects.equals(this.authServerId, that.getAuthServerId());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.authServerId, 42});
  }

  @Generated
  public Long getId() {
    return this.id;
  }

  @Generated
  public String getUserName() {
    return this.userName;
  }

  @Generated
  public String getFirstName() {
    return this.firstName;
  }

  @Generated
  public String getLastName() {
    return this.lastName;
  }

  @Generated
  public String getAuthServerId() {
    return this.authServerId;
  }

  @Generated
  public String getEmail() {
    return this.email;
  }

  @Generated
  public List<Address> getAddresses() {
    return this.addresses;
  }

  @Generated
  public List<PaymentMethod> getPaymentMethods() {
    return this.paymentMethods;
  }

  @Generated
  public LocalDateTime getLastLogin() {
    return this.lastLogin;
  }

  @Generated
  public void setId(final Long id) {
    this.id = id;
  }

  @Generated
  public void setUserName(final String userName) {
    this.userName = userName;
  }

  @Generated
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  @Generated
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  @Generated
  public void setAuthServerId(final String authServerId) {
    this.authServerId = authServerId;
  }

  @Generated
  public void setEmail(final String email) {
    this.email = email;
  }

  @Generated
  public void setAddresses(final List<Address> addresses) {
    this.addresses = addresses;
  }

  @Generated
  public void setPaymentMethods(final List<PaymentMethod> paymentMethods) {
    this.paymentMethods = paymentMethods;
  }

  @Generated
  public void setLastLogin(final LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  @Generated
  public Customer() {
  }
}
