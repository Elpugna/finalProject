package com.applaudostudios.resourceserver.repository;

import com.applaudostudios.resourceserver.model.CheckoutItem;
import com.applaudostudios.resourceserver.model.CheckoutItemPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutItemRepository extends JpaRepository<CheckoutItem, CheckoutItemPk> {
}
