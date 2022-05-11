package com.applaudostudios.resourceserver.service;

import com.applaudostudios.resourceserver.exceptions.ResourceNotFoundException;
import com.applaudostudios.resourceserver.model.Product;
import com.applaudostudios.resourceserver.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@DisplayName("In the Product Service:")
class ProductServiceImplTest {
  @Mock
  private ProductRepository productRepository;
  private ProductService productService;
  private Product product;

  @BeforeEach
  void setup() {
    this.productService = new ProductServiceImpl(this.productRepository);
    this.product = new Product("Spoon", 1.23D);
    this.product.setId(1L);
  }

  @Nested
  @DisplayName("The getProduct method:")
  class GetProd {

    @Test
    @DisplayName("Should return the product with the given Id")
    void getProduct() {
      when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
      Product productExpected = productService.getProduct(1L);
      verify(productRepository).findById(1L);
      assertThat(productExpected.getId(), equalTo(product.getId()));
      assertThat(productExpected.getPrice(), equalTo(product.getPrice()));
      assertThat(productExpected.getName(), equalTo(product.getName()));
    }

    @Test
    @DisplayName("Should throw a \"ResourceNotFoundException\" if no product matches with the given Id")
    void cantGetUserByEmail() {
      when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
      assertThatThrownBy(() -> {
        productService.getProduct(1L);
      }).isInstanceOf(ResourceNotFoundException.class);
      verify(productRepository).findById(1L);
    }
  }

  @Nested
  @DisplayName("The save method:")
  class Save {
    @Test
    @DisplayName("Should save (persist) a given Product")
    void save() {
      productService.save(product);
      ArgumentCaptor<Product> productArgumentCaptor = forClass(Product.class);
      verify(productRepository).save(productArgumentCaptor.capture());
      Product productCaptured = productArgumentCaptor.getValue();
      assertThat(productCaptured.getName(), is(equalTo("Spoon")));
      assertThat(productCaptured.getPrice(), is(equalTo(1.23D)));
    }
  }
}
