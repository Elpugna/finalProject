package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.exceptions.UserAlreadyRegistered;
import com.applaudostudios.resourceserver.model.Address;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.PaymentMethod;
import com.applaudostudios.resourceserver.repository.AddressRepository;
import com.applaudostudios.resourceserver.repository.CheckoutRepository;
import com.applaudostudios.resourceserver.repository.PaymentMethodRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CheckoutServiceImpl implements CheckoutService {
  private final CheckoutRepository checkoutRepository;
  private final CheckoutItemService checkoutItemService;
  private final CustomerService customerService;
  private final AddressRepository addressRepository;
  private final PaymentMethodRepository paymentMethodRepository;

  @Autowired
  public CheckoutServiceImpl(final CheckoutRepository checkoutRepository, final CheckoutItemService checkoutItemService, final CustomerService customerService, final AddressRepository addressRepository, final PaymentMethodRepository paymentMethodRepository) {
    this.checkoutRepository = checkoutRepository;
    this.checkoutItemService = checkoutItemService;
    this.customerService = customerService;
    this.addressRepository = addressRepository;
    this.paymentMethodRepository = paymentMethodRepository;
  }

  public Checkout save(final Checkout checkout) {
    return (Checkout)this.checkoutRepository.save(checkout);
  }

  public Checkout getUserCheckout(final String userEmail) {
    return (Checkout)this.checkoutRepository.findByEmail(userEmail).orElseThrow(() -> {
      return new ResourceNotFoundException("Customer with email: " + userEmail + " does not have a checkout associated", "getUserCheckout() - Checkout");
    });
  }

  public Checkout create(final String userEmail) {
    this.checkoutRepository.findByEmail(userEmail).ifPresent((checkout) -> {
      throw new UserAlreadyRegistered("The user with email: " + userEmail + " already has a cart associated");
    });
    Checkout newCheckout = new Checkout();
    Customer customer = this.customerService.findByEmail(userEmail);
    newCheckout.setUserName(customer.getUserName());
    newCheckout.setEmail(userEmail);
    newCheckout.setCheckoutItems(new ArrayList());
    return this.save(newCheckout);
  }

  public Checkout addItem(final ProductReqDto productReqDto, final String userEmail) {
    Checkout checkout = this.getUserCheckout(userEmail);
    CheckoutItem item = this.checkoutItemService.getCheckoutItem(productReqDto.getId(), checkout);
    if (item == null) {
      item = this.checkoutItemService.createCheckoutItem(productReqDto, checkout);
      checkout.getCheckoutItems().add(item);
    } else {
      Long quantity = productReqDto.getQuantity();
      Long itemQuantity = item.getQuantity();
      this.checkoutItemService.updateQuantity(item, itemQuantity + quantity);
    }

    return this.save(checkout);
  }

  public Checkout updateItem(final ProductReqDto productReqDto, final String userEmail) {
    Checkout checkout = this.getUserCheckout(userEmail);
    CheckoutItem item = this.checkoutItemService.getCheckoutItem(productReqDto.getId(), checkout);
    this.checkoutItemService.updateQuantity(item, productReqDto.getQuantity());
    return this.save(checkout);
  }

  public Checkout removeItem(final Long productId, final String userEmail) {
    Checkout checkout = this.getUserCheckout(userEmail);
    CheckoutItem item = this.checkoutItemService.getCheckoutItem(productId, checkout);
    if (item == null) {
      throw new ResourceNotFoundException("You first must have the item in your checkout in order to delete it", "removeItem() - CheckoutS");
    } else {
      checkout.getCheckoutItems().remove(item);
      if (checkout.getCheckoutItems().isEmpty()) {
        this.checkoutItemService.removeCartItem(item);
        this.checkoutRepository.delete(checkout);
        return null;
      } else {
        this.checkoutItemService.removeCartItem(item);
        this.save(checkout);
        return checkout;
      }
    }
  }

  public void removeCheckout(final Checkout checkout) {
    ArrayList<CheckoutItem> items = new ArrayList(checkout.getCheckoutItems());
    Iterator var3 = items.iterator();

    while(var3.hasNext()) {
      CheckoutItem i = (CheckoutItem)var3.next();
      this.removeItem(i.getCheckoutItemPk().getProduct().getId(), checkout.getEmail());
    }

  }

  public Checkout setAddress(final String address, final String userEmail) {
    Customer customer = this.customerService.findByEmail(userEmail);
    Checkout checkout = this.getUserCheckout(userEmail);
    Address newAddress = new Address(customer, address);
    List<Address> addresses = this.addressRepository.findByCustomer(customer);
    if (addresses.contains(newAddress)) {
      checkout.getAddress().setCheckout((Checkout)null);
      checkout.setAddress(newAddress);
      newAddress.setCheckout(checkout);
      this.addressRepository.saveAllAndFlush(addresses);
      return this.save(checkout);
    } else {
      newAddress = (Address)this.addressRepository.save(newAddress);
      checkout.getAddress().setCheckout((Checkout)null);
      this.addressRepository.save(checkout.getAddress());
      checkout.setAddress(newAddress);
      newAddress.setCheckout(checkout);
      this.addressRepository.saveAndFlush(newAddress);
      return this.save(checkout);
    }
  }

  public Checkout updateAddress(final Long addressId, final String newAddress, final String userEmail) {
    Customer customer = this.customerService.findByEmail(userEmail);
    List<Address> addressList = this.addressRepository.findByCustomer(customer);
    Address address = (Address)this.addressRepository.getById(addressId);
    if (!addressList.contains(address)) {
      throw new ResourceNotFoundException("Address with id: " + addressId + " is not associated with your account", "updateAddress() - CheckoutS");
    } else {
      address.setAddress(newAddress);
      this.addressRepository.save(address);
      Checkout checkout = this.getUserCheckout(userEmail);
      checkout.setAddress(address);
      return checkout;
    }
  }

  public Checkout setPaymentMethod(final String newPayment, final String userEmail) {
    Customer customer = this.customerService.findByEmail(userEmail);
    Checkout checkout = this.getUserCheckout(userEmail);
    PaymentMethod newPaymentMethod = new PaymentMethod(customer, newPayment);
    List<PaymentMethod> paymentList = this.paymentMethodRepository.findByCustomer(customer);
    if (paymentList.contains(newPaymentMethod)) {
      checkout.getPaymentMethod().setCheckout((Checkout)null);
      checkout.setPaymentMethod(newPaymentMethod);
      newPaymentMethod.setCheckout(checkout);
      this.paymentMethodRepository.saveAllAndFlush(paymentList);
      return this.save(checkout);
    } else {
      newPaymentMethod = (PaymentMethod)this.paymentMethodRepository.save(newPaymentMethod);
      checkout.getPaymentMethod().setCheckout((Checkout)null);
      this.paymentMethodRepository.save(checkout.getPaymentMethod());
      checkout.setPaymentMethod(newPaymentMethod);
      newPaymentMethod.setCheckout(checkout);
      this.paymentMethodRepository.saveAndFlush(newPaymentMethod);
      return this.save(checkout);
    }
  }

  public Checkout updatePaymentMethod(final Long paymentMethodId, final String newPaymentMethodName, final String userEmail) {
    Customer customer = this.customerService.findByEmail(userEmail);
    List<PaymentMethod> paymentMethodList = this.paymentMethodRepository.findByCustomer(customer);
    PaymentMethod paymentMethod = (PaymentMethod)this.paymentMethodRepository.getById(paymentMethodId);
    if (!paymentMethodList.contains(paymentMethod)) {
      throw new ResourceNotFoundException("Address with id: " + paymentMethodId + " is not associated with your account", "updatePaymentMethod() - CheckoutS");
    } else {
      paymentMethod.setPaymentMethod(newPaymentMethodName);
      this.paymentMethodRepository.save(paymentMethod);
      Checkout checkout = this.getUserCheckout(userEmail);
      checkout.setPaymentMethod(paymentMethod);
      return checkout;
    }
  }
}
