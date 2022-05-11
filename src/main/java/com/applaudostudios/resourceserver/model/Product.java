package com.applaudostudios.resourceserver.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Generated;

@Entity
@Table(
        name = "Products"
)
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "The name is required for persistance")
  private String name;

  @NotNull(message = "The price is required for persistance")
  private Double price;

  public Product(final String name, final Double price) {
    this.name = name;
    this.price = price;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Product) {
      Product that = (Product)o;
      return Objects.equals(this.id, that.getId());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.getId()});
  }

  @Generated
  public Product() {
  }

  @Generated
  public Long getId() {
    return this.id;
  }

  @Generated
  public String getName() {
    return this.name;
  }

  @Generated
  public Double getPrice() {
    return this.price;
  }

  @Generated
  public void setId(final Long id) {
    this.id = id;
  }

  @Generated
  public void setName(final String name) {
    this.name = name;
  }

  @Generated
  public void setPrice(final Double price) {
    this.price = price;
  }
}
