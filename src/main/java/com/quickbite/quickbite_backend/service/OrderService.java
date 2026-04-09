package com.quickbite.quickbite_backend.service;

import com.quickbite.quickbite_backend.model.*;
import com.quickbite.quickbite_backend.model.OrderStatus;
import com.quickbite.quickbite_backend.notification.NotificationService;
import com.quickbite.quickbite_backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final NotificationService notificationService;

    public Order placeOrder(Long userId, Long restaurantId,
                            List<OrderItem> items, String deliveryAddress) {

        User user = userService.getUserById(userId);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(deliveryAddress);
        order.setStatus(OrderStatus.PENDING);

        double total = 0;
        for (OrderItem item : items) {
            MenuItem menuItem = menuItemService.getMenuItemById(
                    item.getMenuItem().getId()
            );
            item.setPrice(menuItem.getPrice());
            item.setOrder(order);
            total += menuItem.getPrice() * item.getQuantity();
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);
        notificationService.sendOrderConfirmation(savedOrder);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        notificationService.sendStatusUpdate(updated);
        return updated;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}