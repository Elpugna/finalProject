package com.applaudostudios.resourceserver.controller;

import com.applaudostudios.resourceserver.dto.Mapper;
import com.applaudostudios.resourceserver.dto.requestdto.AddressReqDto;
import com.applaudostudios.resourceserver.dto.requestdto.PaymentMethodReqDto;
import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.dto.responsedto.CheckoutResDto;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.service.CheckoutService;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/checkout"})
public class CheckoutController {
  @Autowired
  private CheckoutService checkoutService;
  private final String EMAIL_CLAIM = "email";

  @GetMapping
  public ResponseEntity<CheckoutResDto> getCheckoutInfo(@AuthenticationPrincipal final Jwt principal) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    Checkout checkout = this.checkoutService.getUserCheckout(email);
    CheckoutResDto checkoutResDto = Mapper.checkoutToCheckoutResDto(checkout);
    return new ResponseEntity(checkoutResDto, HttpStatus.OK);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createCheckout(@AuthenticationPrincipal final Jwt principal,@Validated @RequestBody  final ProductReqDto productReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.create(email);
    this.checkoutService.addItem(productReqDto, email);
  }

  @PostMapping({"/item"})
  @ResponseStatus(HttpStatus.OK)
  public void addProduct(@AuthenticationPrincipal final Jwt principal, @Validated @RequestBody  final ProductReqDto productReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.addItem(productReqDto, email);
  }

  @PutMapping({"/item"})
  @ResponseStatus(HttpStatus.OK)
  public void modifyQuantity(@AuthenticationPrincipal final Jwt principal, @RequestBody @Validated final ProductReqDto productReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.updateItem(productReqDto, email);
  }

  @DeleteMapping({"/item/{id}"})
  @ResponseStatus(HttpStatus.OK)
  public void deleteItem(@AuthenticationPrincipal final Jwt principal, @PathVariable(name = "id") @Validated final Long productId) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.removeItem(productId, email);
  }

  @PostMapping({"/address"})
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> setAddress(@AuthenticationPrincipal final Jwt principal, @RequestBody final AddressReqDto addressReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.setAddress(addressReqDto.getAddress(), email);
    return new ResponseEntity("Address added", HttpStatus.OK);
  }

  @PutMapping({"/address"})
  @ResponseStatus(HttpStatus.OK)
  public void modifyAddress(@AuthenticationPrincipal final Jwt principal, @RequestBody final AddressReqDto addressReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.updateAddress(addressReqDto.getId(), addressReqDto.getAddress(), email);
  }

  @PostMapping({"/payment"})
  @ResponseStatus(HttpStatus.CREATED)
  public void setPaymentMethod(@AuthenticationPrincipal final Jwt principal, @RequestBody @Validated @NotBlank final PaymentMethodReqDto paymentMethodReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.setPaymentMethod(paymentMethodReqDto.getPaymentMethod(), email);
  }

  @PutMapping({"/payment"})
  @ResponseStatus(HttpStatus.OK)
  public void modifyPaymentMethod(@AuthenticationPrincipal final Jwt principal, @RequestBody @Validated @Positive final PaymentMethodReqDto paymenntMetReqDto) {
    Map<String, Object> claims = principal.getClaims();
    String email = (String)claims.get("email");
    this.checkoutService.updatePaymentMethod(paymenntMetReqDto.getId(), paymenntMetReqDto.getPaymentMethod(), email);
  }

  @Generated
  public CheckoutController() {
  }
}
