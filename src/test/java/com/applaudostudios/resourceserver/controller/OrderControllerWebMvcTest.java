package com.applaudostudios.resourceserver.controller;

import com.applaudostudios.resourceserver.model.Delivery;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.service.OrderService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(
        controllers = {OrderController.class}
)
@DisplayName("In the Orders Controller:")
class OrderControllerWebMvcTest {
  @MockBean
  private JwtDecoder jwtDecoder;
  @MockBean
  private OrderService orderService;
  @Autowired
  private MockMvc mockmvc;
  Order order;
  Delivery delivery;

  OrderControllerWebMvcTest() {
  }

  @BeforeEach
  void setUp() {
    this.order = new Order("jane@doe.com", 125.63D, "janedoe", "1234-3214-9999", "Visa");
    this.delivery = new Delivery("Cabildo y Juramento");
    this.order.setId(1L);
    this.order.setDelivery(this.delivery);
  }

  @Nested
  @DisplayName("The createOrders endpoint POST:(/api/vX/order:")
  class CreateOrder {
    CreateOrder() {
    }

    @Test
    @DisplayName("Should return forbidden when trying to access without the Jwt Token")
    void forbiddenRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.create(ArgumentMatchers.anyString())).thenReturn(OrderControllerWebMvcTest.this.order);
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/order", new Object[0])).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Should return Forbidden when trying to access with the wrong credentials")
    void invalidAuthorityRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.create(ArgumentMatchers.anyString())).thenReturn(OrderControllerWebMvcTest.this.order);
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/order", new Object[0]).with(SecurityMockMvcRequestPostProcessors.jwt().jwt((builder) -> {
        builder.claim("scope", "Actuator:read").claim("email", "jane@doe.com");
      }))).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Should return status Ok if the operation is successful")
    void validTestRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.create(ArgumentMatchers.anyString())).thenReturn(OrderControllerWebMvcTest.this.order);
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.post("/api/v1/order", new Object[0]).with(SecurityMockMvcRequestPostProcessors.jwt().jwt((builder) -> {
        builder.claim("scope", "email Customer:write").claim("email", "jane@doe.com");
      })).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk());
    }
  }

  @Nested
  @DisplayName("The getOrders endpoint GET:(/api/<version>/order):")
  class GetOrders {
    GetOrders() {
    }

    @Test
    @DisplayName("Should return Unauthorized when trying to access without the Jwt Token")
    void unauthorizedTestRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.getOrders(ArgumentMatchers.anyString())).thenReturn(List.of(OrderControllerWebMvcTest.this.order));
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/order", new Object[0])).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return Forbidden when trying to access with the wrong credentials")
    void inauthorizedTestRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.getOrders(ArgumentMatchers.anyString())).thenReturn(List.of(OrderControllerWebMvcTest.this.order));
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/order", new Object[0]).with(SecurityMockMvcRequestPostProcessors.jwt().jwt((builder) -> {
        builder.claim("scope", "Actuator:read").claim("email", "jane@doe.com");
      }))).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Should return the customer's orders info")
    void validTestRequest() throws Exception {
      Mockito.when(OrderControllerWebMvcTest.this.orderService.getOrders(ArgumentMatchers.anyString())).thenReturn(List.of(OrderControllerWebMvcTest.this.order));
      OrderControllerWebMvcTest.this.mockmvc.perform(MockMvcRequestBuilders.get("/api/v1/order", new Object[0]).with(SecurityMockMvcRequestPostProcessors.jwt().jwt((builder) -> {
        builder.claim("scope", "Customer:read").claim("email", "jane@doe.com");
      })).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$.[0].userName", new Object[0]).value("janedoe")).andExpect(MockMvcResultMatchers.jsonPath("$[0]totalPrice", new Object[0]).value(125.63D));
    }
  }
}
