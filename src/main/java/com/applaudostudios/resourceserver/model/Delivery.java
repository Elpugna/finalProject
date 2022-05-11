package com.applaudostudios.resourceserver.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import lombok.Generated;

@Embeddable
public class Delivery {
  @NotBlank(
          message = "Delivery's address must be present"
  )
  private String address;
  private Delivery.Status status;

  public Delivery(final String address) {
    this.address = address;
    this.status = Delivery.Status.PREPARING;
  }

  @Generated
  public String getAddress() {
    return this.address;
  }

  @Generated
  public Delivery.Status getStatus() {
    return this.status;
  }

  @Generated
  public void setAddress(final String address) {
    this.address = address;
  }

  @Generated
  public void setStatus(final Delivery.Status status) {
    this.status = status;
  }

  @Generated
  public Delivery() {
  }

  public static enum Status {
    PREPARING,
    DELIVERED,
    RECEIVED;

    private Status() {
    }
  }
}
