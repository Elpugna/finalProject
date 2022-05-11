package com.applaudostudios.resourceserver.dto.responsedto;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
public class OrderResDto {
  private String userName;
  private String email;
  private String address;
  private String paymentMethod;
  private Double totalPrice;
  private Set<ItemResDto> products;

}
