package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.exceptions.UserAlreadyRegistered;
import com.applaudostudios.resourceserver.model.Address;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.Customer;
import com.applaudostudios.resourceserver.model.PaymentMethod;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.AddressRepository;
import com.applaudostudios.resourceserver.repository.CheckoutRepository;
import com.applaudostudios.resourceserver.repository.PaymentMethodRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Checkout Service")
class CheckoutServiceImplTest {
  CheckoutService checkoutService;
  @Mock
  CheckoutRepository checkoutRepository;
  @Mock
  CheckoutItemService checkoutItemService;
  @Mock
  AddressRepository addressRepository;
  @Mock
  CustomerService customerService;
  @Mock
  PaymentMethodRepository paymentMethodRepository;
  Checkout checkout;
  CheckoutItem checkoutItem;
  Product product;
  Customer customer;
  Address address;
  PaymentMethod paymentMethod;

  CheckoutServiceImplTest() {
  }

  @BeforeEach
  public void setup() {
    this.checkoutService = new CheckoutServiceImpl(this.checkoutRepository, this.checkoutItemService, this.customerService, this.addressRepository, this.paymentMethodRepository);
    this.paymentMethod = new PaymentMethod(this.customer, "Debt");
    this.paymentMethod.setId(6L);
    this.address = new Address(this.customer, "Avenue");
    this.address.setId(6L);
    this.customer = new Customer("1234-4321", "jane@doe.com", "janedoe", "Doe", "Jane");
    this.customer.setId(1L);
    this.customer.getAddresses().add(this.address);
    this.customer.getPaymentMethods().add(this.paymentMethod);
    this.product = new Product("Spoon", 3.25D);
    this.product.setId(2L);
    this.checkout = new Checkout("jane@doe.com", "janedoe");
    this.checkout.setId(8L);
    this.checkout.setAddress(this.address);
    this.address.setCheckout(this.checkout);
    this.checkout.setPaymentMethod(this.paymentMethod);
    this.paymentMethod.setCheckout(this.checkout);
    this.checkoutItem = new CheckoutItem(this.checkout, this.product, 15L);
    this.checkout.getCheckoutItems().add(this.checkoutItem);
  }

  @Nested
  @DisplayName("The updatePaymentMethod method:")
  class UpdatePaymentMethod {
    UpdatePaymentMethod() {
    }

    @Disabled
    @Test
    @DisplayName("Should change the checkout's payment method")
    void updatePaymentMethod() {
      CheckoutServiceImplTest.this.checkout.setPaymentMethod(new PaymentMethod(CheckoutServiceImplTest.this.customer, "Bitcoin"));
      CheckoutServiceImplTest.this.paymentMethod.setCheckout(null);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.paymentMethodRepository.getById(ArgumentMatchers.anyLong())).thenReturn(CheckoutServiceImplTest.this.paymentMethod);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.save(ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkout);
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getPaymentMethod().getPaymentMethod(), Matchers.is(Matchers.equalTo("Bitcoin")));
      CheckoutServiceImplTest.this.checkoutService.updatePaymentMethod(6L, "Etherum", "jane@doe.com");
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getPaymentMethod().getPaymentMethod(), Matchers.is(Matchers.equalTo("Debt")));
    }

    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" when there is no payment method to update")
    void FailToUpdateAddress() {
      CheckoutServiceImplTest.this.checkout.setPaymentMethod(new PaymentMethod(CheckoutServiceImplTest.this.customer, "Yuh Gih Oh cards"));
      CheckoutServiceImplTest.this.paymentMethod.setCheckout(null);
      Mockito.when(CheckoutServiceImplTest.this.paymentMethodRepository.getById(ArgumentMatchers.anyLong())).thenReturn(null);
      Assertions.assertThrows(RuntimeException.class, () -> {
        CheckoutServiceImplTest.this.checkoutService.updatePaymentMethod(12L, "Faith", "jane@doe.com");
      });
    }
  }

  @Nested
  @DisplayName("The setPayment method:")
  class setPaymentMethod {
    setPaymentMethod() {
    }

    @Test
    @DisplayName("Should set the checkout's payment method")
    void setPaymentMethod() {
      CheckoutServiceImplTest.this.checkout.setAddress(CheckoutServiceImplTest.this.address);
      CheckoutServiceImplTest.this.address.setCheckout(CheckoutServiceImplTest.this.checkout);
      Mockito.when(CheckoutServiceImplTest.this.customerService.findByEmail(ArgumentMatchers.anyString())).thenReturn(CheckoutServiceImplTest.this.customer);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.paymentMethodRepository.save(ArgumentMatchers.any(PaymentMethod.class))).thenReturn(CheckoutServiceImplTest.this.paymentMethod);
      CheckoutServiceImplTest.this.paymentMethod.setCheckout(CheckoutServiceImplTest.this.checkout);
      CheckoutServiceImplTest.this.checkoutService.setPaymentMethod("Debt", "jane@doe.com");
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getPaymentMethod(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.paymentMethod)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.paymentMethod.getCheckout(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.checkout)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getPaymentMethod().getPaymentMethod(), Matchers.is(Matchers.equalTo("Debt")));
    }
  }

  @Nested
  @DisplayName("The updateAddress method:")
  class UpdateAddress {
    UpdateAddress() {
    }

    @Disabled
    @Test
    @DisplayName("Should change the checkout's address")
    void updateAddress() {
      CheckoutServiceImplTest.this.checkout.setAddress(new Address(CheckoutServiceImplTest.this.customer, "Boulevard of broken dreams"));
      CheckoutServiceImplTest.this.address.setCheckout(null);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.addressRepository.getById(ArgumentMatchers.anyLong())).thenReturn(CheckoutServiceImplTest.this.address);
      Mockito.when(CheckoutServiceImplTest.this.addressRepository.findByCustomer(CheckoutServiceImplTest.this.customer)).thenReturn(List.of(CheckoutServiceImplTest.this.address));
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.save(ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkout);
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getAddress().getAddress(), Matchers.is(Matchers.equalTo("Boulevard of broken dreams")));
      CheckoutServiceImplTest.this.checkoutService.updateAddress(6L, "Avenue", "jane@doe.com");
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getAddress().getAddress(), Matchers.is(Matchers.equalTo("Avenue")));
    }

    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" when there is no address to update")
    void FailToUpdateAddress() {
      CheckoutServiceImplTest.this.checkout.setAddress(new Address(CheckoutServiceImplTest.this.customer, "Boulevard of broken dreams"));
      CheckoutServiceImplTest.this.address.setCheckout(null);
      Mockito.when(CheckoutServiceImplTest.this.addressRepository.getById(ArgumentMatchers.anyLong())).thenReturn(null);
      Assertions.assertThrows(RuntimeException.class, () -> {
        CheckoutServiceImplTest.this.checkoutService.updateAddress(12L, "Street", "jane@doe.com");
      });
    }
  }

  @Nested
  @DisplayName("The setAddress method:")
  class setAddress {
    setAddress() {
    }

    @Test
    @DisplayName("Should set the checkout's address")
    void setAddress() {
      CheckoutServiceImplTest.this.checkout.setPaymentMethod(CheckoutServiceImplTest.this.paymentMethod);
      CheckoutServiceImplTest.this.paymentMethod.setCheckout(CheckoutServiceImplTest.this.checkout);
      Mockito.when(CheckoutServiceImplTest.this.customerService.findByEmail(ArgumentMatchers.anyString())).thenReturn(CheckoutServiceImplTest.this.customer);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.addressRepository.save(ArgumentMatchers.any(Address.class))).thenReturn(CheckoutServiceImplTest.this.address);
      CheckoutServiceImplTest.this.address.setCheckout(CheckoutServiceImplTest.this.checkout);
      CheckoutServiceImplTest.this.checkoutService.setAddress("Avenue", "jane@doe.com");
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getAddress(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.address)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.address.getCheckout(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.checkout)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getAddress().getAddress(), Matchers.is(Matchers.equalTo("Avenue")));
    }
  }

  @Nested
  @DisplayName("The removeCheckout method:")
  class RemoveCheckout {
    RemoveCheckout() {
    }

    @Test
    @DisplayName("Should remove the checkout and all it's items")
    void removeCheckout() {
      Product newProd = new Product("Onion", 6.0D);
      newProd.setId(99L);
      CheckoutServiceImplTest.this.checkout.getCheckoutItems().add(new CheckoutItem(CheckoutServiceImplTest.this.checkout, newProd, 3L));
      newProd = new Product("Orange", 7.8D);
      newProd.setId(75L);
      CheckoutServiceImplTest.this.checkout.getCheckoutItems().add(new CheckoutItem(CheckoutServiceImplTest.this.checkout, newProd, 14L));
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
      ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
      ArgumentCaptor<CheckoutItem> emailCaptor = ArgumentCaptor.forClass(CheckoutItem.class);
      ((CheckoutItemService)Mockito.doNothing().when(CheckoutServiceImplTest.this.checkoutItemService)).removeCartItem((CheckoutItem)ArgumentMatchers.any(CheckoutItem.class));
      CheckoutServiceImplTest.this.checkoutService.removeCheckout(CheckoutServiceImplTest.this.checkout);
      ((CheckoutItemService)Mockito.verify(CheckoutServiceImplTest.this.checkoutItemService, Mockito.times(3))).removeCartItem((CheckoutItem)ArgumentMatchers.any(CheckoutItem.class));
      ((CheckoutItemService)Mockito.verify(CheckoutServiceImplTest.this.checkoutItemService, Mockito.times(3))).getCheckoutItem((Long)idCaptor.capture(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout));
      List<Long> idValuesList = idCaptor.getAllValues();
      MatcherAssert.assertThat(idValuesList.size(), Matchers.is(Matchers.equalTo(3)));
      MatcherAssert.assertThat((Long)idValuesList.get(0), Matchers.is(Matchers.equalTo(2L)));
      MatcherAssert.assertThat((Long)idValuesList.get(1), Matchers.is(Matchers.equalTo(99L)));
      MatcherAssert.assertThat((Long)idValuesList.get(2), Matchers.is(Matchers.equalTo(75L)));
    }
  }

  @Nested
  @DisplayName("The removeItem method:")
  class RemoveItem {
    RemoveItem() {
    }

    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" if the item is not present in the checkout")
    void invalidRemoveItem() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(null);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      AssertionsForClassTypes.assertThatThrownBy(() -> {
        CheckoutServiceImplTest.this.checkoutService.removeItem(5L, "jane@doe.com");
      }).isInstanceOf(ResourceNotFoundException.class);
      ((CheckoutItemService)Mockito.verify(CheckoutServiceImplTest.this.checkoutItemService)).getCheckoutItem(5L, CheckoutServiceImplTest.this.checkout);
    }

    @Test
    @DisplayName("Should remove the item with the given Id")
    void removeItem() {
      CheckoutServiceImplTest.this.checkout.getCheckoutItems().add(new CheckoutItem(CheckoutServiceImplTest.this.checkout, new Product("Onion", 6.0D), 3L));
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
      ((CheckoutItemService)Mockito.doAnswer((invocation) -> {
        MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkoutItem.getQuantity(), Matchers.is(Matchers.equalTo(15L)));
        CheckoutServiceImplTest.this.checkoutItem.getCheckoutItemPk().getCheckout().getCheckoutItems().remove(CheckoutServiceImplTest.this.checkoutItem);
        CheckoutServiceImplTest.this.checkoutItem = null;
        return null;
      }).when(CheckoutServiceImplTest.this.checkoutItemService)).removeCartItem((CheckoutItem)ArgumentMatchers.any(CheckoutItem.class));
      Checkout resultCheckout = CheckoutServiceImplTest.this.checkoutService.removeItem(6L, "jane@doe.com");
      MatcherAssert.assertThat(resultCheckout, Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.checkout)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkoutItem, Matchers.is(Matchers.equalTo(null)));
      MatcherAssert.assertThat(resultCheckout.getCheckoutItems().size(), Matchers.is(Matchers.equalTo(1)));
      MatcherAssert.assertThat(((CheckoutItem)resultCheckout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(3L)));
      MatcherAssert.assertThat(((CheckoutItem)resultCheckout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(3L)));
    }

    @Test
    @DisplayName("Should remove the item with the given Id, if there are no more items, then also removes the checkout")
    void removeItemAndCheckout() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
      (Mockito.doAnswer((invocation) -> {
        MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getEmail(), Matchers.is(Matchers.equalTo("jane@doe.com")));
        CheckoutServiceImplTest.this.checkout = null;
        return null;
      }).when(CheckoutServiceImplTest.this.checkoutRepository)).delete(ArgumentMatchers.any(Checkout.class));
      ((CheckoutItemService)Mockito.doAnswer((invocation) -> {
        MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkoutItem.getQuantity(), Matchers.is(Matchers.equalTo(15L)));
        CheckoutServiceImplTest.this.checkoutItem = null;
        return null;
      }).when(CheckoutServiceImplTest.this.checkoutItemService)).removeCartItem(ArgumentMatchers.any(CheckoutItem.class));
      Checkout deletedCheckout = CheckoutServiceImplTest.this.checkoutService.removeItem(6L, "jane@doe.com");
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout, Matchers.is(Matchers.equalTo(null)));
      MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkoutItem, Matchers.is(Matchers.equalTo(null)));
      MatcherAssert.assertThat(deletedCheckout, Matchers.is(Matchers.equalTo(null)));
    }
  }

  @Nested
  @DisplayName("The updateItem method:")
  class UpdateItem {
    private ProductReqDto productReqDto;

    UpdateItem() {
    }

    @BeforeEach
    public void setUpAddItem() {
      this.productReqDto = new ProductReqDto(2L, 42L);
    }

    @Test
    @DisplayName("Should update the selected item's quantity")
    void updateItem() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.save(ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkout);
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
      Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.updateQuantity(ArgumentMatchers.any(CheckoutItem.class), ArgumentMatchers.anyLong())).thenAnswer(new Answer() {
        public Object answer(final InvocationOnMock invocation) {
          (CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).setQuantity(42L);
          return CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0);
        }
      });
      MatcherAssert.assertThat((CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(15L)));
      CheckoutServiceImplTest.this.checkoutService.updateItem(this.productReqDto, "jane@doe.com");
      MatcherAssert.assertThat((CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(42L)));
    }
  }

  @Nested
  @DisplayName("The addItem method:")
  class AddItem {
    private ProductReqDto productReqDto;

    AddItem() {
    }

    @BeforeEach
    public void setUpAddItem() {
      this.productReqDto = new ProductReqDto(2L, 3L);
    }

    @Nested
    @DisplayName("If the item was already in the checkout")
    class ItemPresent {
      ItemPresent() {
      }

      @Test
      @DisplayName("Should add the product to the checkout summing up the desired quantity")
      void addItem() {
        new CheckoutItem(null, CheckoutServiceImplTest.this.product, 3L);
        Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
        Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
        Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.updateQuantity(ArgumentMatchers.any(CheckoutItem.class), ArgumentMatchers.anyLong())).thenAnswer(new Answer() {
          public Object answer(final InvocationOnMock invocation) {
            (CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).setQuantity(30L);
            return CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0);
          }
        });
        Mockito.when(CheckoutServiceImplTest.this.checkoutService.save(ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkout);
        MatcherAssert.assertThat((CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(15L)));
        CheckoutServiceImplTest.this.checkoutService.addItem(AddItem.this.productReqDto, "jane@doe.com");
        ((CheckoutItemService)Mockito.verify(CheckoutServiceImplTest.this.checkoutItemService)).getCheckoutItem(2L, CheckoutServiceImplTest.this.checkout);
        MatcherAssert.assertThat((CheckoutServiceImplTest.this.checkout.getCheckoutItems().get(0)).getQuantity(), Matchers.is(Matchers.equalTo(30L)));
      }
    }

    @Nested
    @DisplayName("If the item was not previously in the checkout")
    class Item404 {
      Item404() {
      }

      @Test
      @DisplayName("Should add the product to the checkout")
      void addINewItem() {
        CheckoutServiceImplTest.this.checkout.getCheckoutItems().remove(CheckoutServiceImplTest.this.checkoutItem);
        CheckoutServiceImplTest.this.checkoutItem.getCheckoutItemPk().setCheckout(null);
        Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
        Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.getCheckoutItem(ArgumentMatchers.anyLong(), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(null);
        Mockito.when(CheckoutServiceImplTest.this.checkoutItemService.createCheckoutItem(ArgumentMatchers.any(ProductReqDto.class), ArgumentMatchers.eq(CheckoutServiceImplTest.this.checkout))).thenReturn(CheckoutServiceImplTest.this.checkoutItem);
        Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.save(ArgumentMatchers.any(Checkout.class))).thenReturn(CheckoutServiceImplTest.this.checkout);
        MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getCheckoutItems().size(), Matchers.is(Matchers.equalTo(0)));
        MatcherAssert.assertThat(CheckoutServiceImplTest.this.checkout.getCheckoutItems().contains(CheckoutServiceImplTest.this.checkoutItem), Matchers.is(Matchers.equalTo(false)));
        Checkout actualCheckout = CheckoutServiceImplTest.this.checkoutService.addItem(AddItem.this.productReqDto, "jane@doe.com");
        MatcherAssert.assertThat(actualCheckout.getCheckoutItems().size(), Matchers.is(Matchers.equalTo(1)));
        MatcherAssert.assertThat(actualCheckout.getCheckoutItems().contains(CheckoutServiceImplTest.this.checkoutItem), Matchers.is(Matchers.equalTo(true)));
      }
    }
  }

  @Nested
  @DisplayName("The create method:")
  class Create {
    Create() {
    }

    @Test
    @DisplayName("Should throw a \"UserAlreadyRegistered\" when customer already has a checkout associated")
    void failToCreate() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      AssertionsForClassTypes.assertThatThrownBy(() -> {
        CheckoutServiceImplTest.this.checkoutService.create(ArgumentMatchers.anyString());
      }).isInstanceOf(UserAlreadyRegistered.class);
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).findByEmail(ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Should create a new checkout with a given customer email if it does not have one associated")
    void create() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
      Mockito.when(CheckoutServiceImplTest.this.customerService.findByEmail(ArgumentMatchers.anyString())).thenReturn(CheckoutServiceImplTest.this.customer);
      CheckoutServiceImplTest.this.checkoutService.create("jane@doe.com");
      ArgumentCaptor<Checkout> checkoutArgumentCaptor = ArgumentCaptor.forClass(Checkout.class);
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).save(checkoutArgumentCaptor.capture());
      Checkout capturedCheckout = checkoutArgumentCaptor.getValue();
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).findByEmail("jane@doe.com");
      (Mockito.verify(CheckoutServiceImplTest.this.customerService)).findByEmail("jane@doe.com");
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).save(ArgumentMatchers.any(Checkout.class));
      MatcherAssert.assertThat(capturedCheckout.getEmail(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.customer.getEmail())));
      MatcherAssert.assertThat(capturedCheckout.getCheckoutItems().isEmpty(), Matchers.is(Matchers.equalTo(true)));
      MatcherAssert.assertThat(capturedCheckout.getUserName(), Matchers.is(Matchers.equalTo(CheckoutServiceImplTest.this.customer.getUserName())));
    }
  }

  @Nested
  @DisplayName("The getUserCheckout method:")
  class GetUserCheckout {
    GetUserCheckout() {
    }

    @Test
    @DisplayName("Should return the customer's checkout if it has one registered")
    void getUserCheckout() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(CheckoutServiceImplTest.this.checkout));
      Checkout actualCheckout = CheckoutServiceImplTest.this.checkoutService.getUserCheckout("jane@doe.com");
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).findByEmail("jane@doe.com");
      MatcherAssert.assertThat(Objects.equals(actualCheckout, CheckoutServiceImplTest.this.checkout), Matchers.is(true));
      MatcherAssert.assertThat(actualCheckout.getEmail(), Matchers.is(Matchers.equalTo("jane@doe.com")));
      MatcherAssert.assertThat(actualCheckout.getAddress().getAddress(), Matchers.is(Matchers.equalTo("Avenue")));
      MatcherAssert.assertThat(actualCheckout.getPaymentMethod().getPaymentMethod(), Matchers.is(Matchers.equalTo("Debt")));
    }

    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" when there is no checkout registered ")
    void failToGetCheckout() {
      Mockito.when(CheckoutServiceImplTest.this.checkoutRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
      AssertionsForClassTypes.assertThatThrownBy(() -> {
        CheckoutServiceImplTest.this.checkoutService.getUserCheckout(ArgumentMatchers.anyString());
      }).isInstanceOf(ResourceNotFoundException.class);
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).findByEmail(ArgumentMatchers.anyString());
    }
  }

  @Nested
  @DisplayName("The save method:")
  class Save {
    Save() {
    }

    @Test
    @DisplayName("Should save (persist) a given Checkout")
    void save() {
      CheckoutServiceImplTest.this.checkoutService.save(CheckoutServiceImplTest.this.checkout);
      ArgumentCaptor<Checkout> checkoutArgumentCaptor = ArgumentCaptor.forClass(Checkout.class);
      (Mockito.verify(CheckoutServiceImplTest.this.checkoutRepository)).save(checkoutArgumentCaptor.capture());
      Checkout capturedCheckout = checkoutArgumentCaptor.getValue();
      MatcherAssert.assertThat(capturedCheckout.getId(), Matchers.is(Matchers.equalTo(8L)));
      MatcherAssert.assertThat(capturedCheckout.getEmail(), Matchers.is(Matchers.equalTo("jane@doe.com")));
      MatcherAssert.assertThat(capturedCheckout.getAddress().getAddress(), Matchers.is(Matchers.equalTo("Avenue")));
      MatcherAssert.assertThat(capturedCheckout.getPaymentMethod().getPaymentMethod(), Matchers.is(Matchers.equalTo("Debt")));
    }
  }
}
