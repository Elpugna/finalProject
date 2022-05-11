package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.CheckoutItemPk;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.CheckoutItemRepository;

import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Checkout Item Service:")
class CheckoutItemServiceImplTest {
  @Mock
  CheckoutItemRepository checkoutItemRepository;
  @Mock
  ProductService productService;
  CheckoutItemService checkoutItemService;
  CheckoutItem item;
  Product product;
  Checkout checkout;

  @BeforeEach
  public void setUp() {
    checkoutItemService = new CheckoutItemServiceImpl(checkoutItemRepository, productService);
    product = new Product("Spoon", 9.36D);
    product.setId(2L);
    checkout = new Checkout("jane@doe.com", "janedoe");
    checkout.setId(1L);
    item = new CheckoutItem(checkout, product, 3L);
  }

  @Nested
  @DisplayName("The removeCartItem method:")
  class RemoveCartItem {
    @Test
    void removeCartItem() {
      doAnswer((invocation) -> {
        assertThat(item.getCheckoutItemPk().getProduct().getId(), is(equalTo(2L)));
        assertThat(item.getCheckoutItemPk().getCheckout().getId(), is(equalTo(1L)));
        assertThat(item.getQuantity(), is(equalTo(3L)));
        item = null;
        return null;
      }).when(checkoutItemRepository).delete(ArgumentMatchers.any(CheckoutItem.class));
      checkoutItemService.removeCartItem(item);
      assertThat(item, is(equalTo((Object)null)));
    }
  }

  @Nested
  @DisplayName("The updateQuantity method:")
  class UpdateQuantity {
    @Test
    void updateQuantity() {
      assertThat(item.getQuantity(), is(equalTo(3L)));
      checkoutItemService.updateQuantity(item, 50L);
      ArgumentCaptor<CheckoutItem> itemCapturer = ArgumentCaptor.forClass(CheckoutItem.class);
      (verify(checkoutItemRepository)).save(itemCapturer.capture());
      CheckoutItem actualItem = itemCapturer.getValue();
      assertThat(actualItem.getQuantity(), is(equalTo(50L)));
    }
  }

  @Nested
  @DisplayName("The createCheckoutItem method:")
  class CreateCheckoutItem {
    @Test
    @DisplayName("Should create a Checkout item given the product id, quantity and the checkout it belongs")
    void createCheckoutItem() {
      ProductReqDto productReq = new ProductReqDto(2L, 3L);
      when(productService.getProduct(ArgumentMatchers.anyLong())).thenReturn(product);
      checkoutItemService.createCheckoutItem(productReq, checkout);
      ArgumentCaptor<CheckoutItem> itemCapturer = ArgumentCaptor.forClass(CheckoutItem.class);
      (verify(checkoutItemRepository)).save(itemCapturer.capture());
      CheckoutItem actualItem = itemCapturer.getValue();
      assertThat(actualItem.getQuantity(), is(equalTo(item.getQuantity())));
      assertThat(actualItem.getCheckoutItemPk().getCheckout().getId(), is(equalTo(item.getCheckoutItemPk().getCheckout().getId())));
      assertThat(actualItem.getCheckoutItemPk().getProduct().getId(), is(equalTo(item.getCheckoutItemPk().getProduct().getId())));
    }
  }

  @Nested
  @DisplayName("The getCheckoutItem method:")
  class GetCheckoutItem {
    @Test
    @DisplayName("Should return the product item (checkout item) of a given checkout")
    void getCheckoutItem() {
      when(productService.getProduct(ArgumentMatchers.anyLong())).thenReturn(product);
      when(checkoutItemRepository.findById(ArgumentMatchers.any(CheckoutItemPk.class))).thenReturn(Optional.of(item));
      CheckoutItem actualItem = checkoutItemService.getCheckoutItem(2L, checkout);
      ArgumentCaptor<CheckoutItemPk> itemPkArgumentCaptor = ArgumentCaptor.forClass(CheckoutItemPk.class);
      (verify(checkoutItemRepository)).findById(itemPkArgumentCaptor.capture());
      CheckoutItemPk capturedValue = itemPkArgumentCaptor.getValue();
      assertThat(capturedValue.getProduct().getId(), is(equalTo(product.getId())));
      assertThat(capturedValue.getCheckout().getId(), is(equalTo(checkout.getId())));
      assertThat(Objects.equals(actualItem, item), is(equalTo(true)));
    }

    @Test
    @DisplayName("Should return null if the checkout does not have that product in it")
    void failToGetCheckoutItem() {
      when(productService.getProduct(ArgumentMatchers.anyLong())).thenReturn(product);
      when(checkoutItemRepository.findById(ArgumentMatchers.any(CheckoutItemPk.class))).thenReturn(Optional.empty());
      CheckoutItem actualItem = checkoutItemService.getCheckoutItem(2L, checkout);
      ArgumentCaptor<CheckoutItemPk> itemPkArgumentCaptor = ArgumentCaptor.forClass(CheckoutItemPk.class);
      verify(checkoutItemRepository).findById(itemPkArgumentCaptor.capture());
      CheckoutItemPk capturedValue = itemPkArgumentCaptor.getValue();
      assertThat(capturedValue.getProduct().getId(), is(equalTo(product.getId())));
      assertThat(capturedValue.getCheckout().getId(), is(equalTo(checkout.getId())));
      assertThat(actualItem, is(equalTo((Object)null)));
    }
  }

  @Nested
  @DisplayName("The save method:")
  class Save {
    @Test
    @DisplayName("Should save (persist) a given Checkout Item")
    void save() {
      when(checkoutItemRepository.save(ArgumentMatchers.any(CheckoutItem.class))).thenReturn(item);
      CheckoutItem actualItem = checkoutItemService.save(item);
      verify(checkoutItemRepository).save(item);
      assertThat(Objects.equals(actualItem, item), is(equalTo(true)));
      assertThat(actualItem.getQuantity(), is(equalTo(item.getQuantity())));
    }
  }
}
