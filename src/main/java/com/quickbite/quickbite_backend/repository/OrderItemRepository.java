package com.quickbite.quickbite_backend.repository;

import com.quickbite.quickbite_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get all items inside a specific order
    // Used when displaying order details
    List<OrderItem> findByOrderId(Long orderId);
}