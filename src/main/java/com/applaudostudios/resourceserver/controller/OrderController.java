package com.applaudostudios.resourceserver.controller;
import com.applaudostudios.resourceserver.dto.Mapper;
import com.applaudostudios.resourceserver.dto.responsedto.OrderResDto;
import com.applaudostudios.resourceserver.model.Order;
import com.applaudostudios.resourceserver.service.OrderService;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/order"})
public class OrderController {
  @Autowired
  private OrderService orderService;

  @GetMapping
  public ResponseEntity<List<OrderResDto>> getOrders(@AuthenticationPrincipal final Jwt principal) {
    String email = (String)principal.getClaims().get("email");
    List<Order> orders = this.orderService.getOrders(email);
    List<OrderResDto> orderResDtoList = Mapper.ordersToOrderRestDtoList(orders);
    return ResponseEntity.ok(orderResDtoList);
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@AuthenticationPrincipal final Jwt principal) {
    String email = (String)principal.getClaims().get("email");
    Order order = this.orderService.create(email);
    return ResponseEntity.ok(order);
  }

  @Generated
  public OrderController() {
  }
}
