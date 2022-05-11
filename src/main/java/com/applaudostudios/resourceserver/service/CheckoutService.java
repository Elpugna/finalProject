package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.model.Checkout;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CheckoutService {
  Checkout save(Checkout checkout);

  Checkout getUserCheckout(String userEmail);

  Checkout create(String userEmail);

  Checkout addItem(ProductReqDto productReqDto, String userEmail);

  Checkout updateItem(ProductReqDto productReqDto, String userEmail);

  Checkout removeItem(Long productId, String userEmail);

  void removeCheckout(Checkout checkout);

  Checkout setAddress(@NotBlank(message = "Address can not be blank.") String address, String userEmail);

  Checkout updateAddress(@Positive(message = "Address Id must be positive or greater than 0 ") Long addressId, @NotBlank(message = "Address name must be provided in order to update it ") String newAddress, String userEmail);

  Checkout setPaymentMethod(@NotBlank(message = "Payment method can not be blank") String paymentMethod, String userEmail);

  Checkout updatePaymentMethod(@Positive(message = "Payment Id mus be positive or greater than 0") Long paymentMethodId, @NotBlank(message = "Payment method must be provided in order to update it") String paymentMethod, String userEmail);
}