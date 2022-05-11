package com.applaudostudios.resourceserver.dto.requestdto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
public class PaymentMethodReqDto {
  private String paymentMethod;
  private Long id;

}