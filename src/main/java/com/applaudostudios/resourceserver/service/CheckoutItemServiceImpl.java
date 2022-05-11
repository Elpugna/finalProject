package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.dto.requestdto.ProductReqDto;
import com.applaudostudios.resourceserver.model.Checkout;
import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.CheckoutItemPk;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.CheckoutItemRepository;
import javax.validation.constraints.Min;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CheckoutItemServiceImpl implements CheckoutItemService {
  private CheckoutItemRepository checkoutItemRepository;
  private ProductService productService;

  @Autowired
  public CheckoutItemServiceImpl(final CheckoutItemRepository checkoutItemRepository, final ProductService productService) {
    this.checkoutItemRepository = checkoutItemRepository;
    this.productService = productService;
  }

  public CheckoutItem save(final CheckoutItem item) {
    return (CheckoutItem)this.checkoutItemRepository.save(item);
  }

  public CheckoutItem getCheckoutItem(final Long productId, final Checkout checkout) {
    Product product = this.productService.getProduct(productId);
    CheckoutItemPk itemPk = new CheckoutItemPk(checkout, product);
    return (CheckoutItem)this.checkoutItemRepository.findById(itemPk).orElse(null);
  }

  public CheckoutItem createCheckoutItem(final ProductReqDto productReqDto, final Checkout checkout) {
    Product product = this.productService.getProduct(productReqDto.getId());
    CheckoutItem item = new CheckoutItem(checkout, product, productReqDto.getQuantity());
    return this.save(item);
  }

  public CheckoutItem updateQuantity(final CheckoutItem item, @Min(value = 1L,message = "The total quantity mus be greater than 0") final Long quantity) {
    item.setQuantity(quantity);
    return this.save(item);
  }

  public void removeCartItem(final CheckoutItem checkoutItem) {
    this.checkoutItemRepository.delete(checkoutItem);
  }

  @Generated
  public CheckoutItemServiceImpl() {
  }
}
