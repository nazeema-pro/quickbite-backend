package com.quickbite.quickbite_backend.repository;

import com.quickbite.quickbite_backend.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // Get all menu items for a specific restaurant
    // Used when user clicks on a restaurant to see its menu
    List<MenuItem> findByRestaurantId(Long restaurantId);

    // Get only available items for a restaurant
    // Hides items marked as unavailable
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);

    // Search food items by name across all restaurants
    // Used in the search bar
    List<MenuItem> findByNameContainingIgnoreCase(String name);

    // Get items by category like Starters, Mains, Desserts
    List<MenuItem> findByCategory(String category);
}