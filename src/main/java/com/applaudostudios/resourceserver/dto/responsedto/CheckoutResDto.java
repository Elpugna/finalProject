package com.applaudostudios.resourceserver.dto.responsedto;

import lombok.*;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@ToString
public class CheckoutResDto {
  private String userName;
  private String email;
  private String address;
  private String paymentMethod;
  private Set<ItemResDto> products;


}
