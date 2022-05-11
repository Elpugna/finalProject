package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CheckoutItemService {
  CheckoutItem save(CheckoutItem item);

  CheckoutItem createCheckoutItem(ProductReqDto productReqDTO, Checkout checkout);

  CheckoutItem getCheckoutItem(Long productId, Checkout checkout);

  void removeCartItem(CheckoutItem checkoutItem);

  CheckoutItem updateQuantity(CheckoutItem item, @Min(value = 1L,message = "The total quantity mus be greater than 0") Long quantity);
}
