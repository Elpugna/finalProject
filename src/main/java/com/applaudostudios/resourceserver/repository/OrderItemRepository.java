package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.OrderItem;
import com.applaudostudios.resourceserver.model.OrderItemPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPk> {
}
