package com.quickbite.quickbite_backend.controller;

import com.quickbite.quickbite_backend.model.Order;
import com.quickbite.quickbite_backend.model.OrderItem;
import com.quickbite.quickbite_backend.model.OrderStatus;
import com.quickbite.quickbite_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long restaurantId = Long.valueOf(request.get("restaurantId").toString());
        String deliveryAddress = request.get("deliveryAddress").toString();
        List<OrderItem> items = (List<OrderItem>) request.get("items");
        return ResponseEntity.ok(
                orderService.placeOrder(userId, restaurantId, items, deliveryAddress));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getRestaurantOrders(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getRestaurantOrders(restaurantId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}