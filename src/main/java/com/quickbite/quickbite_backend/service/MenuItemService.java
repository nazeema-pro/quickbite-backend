package com.quickbite.quickbite_backend.service;

import com.quickbite.quickbite_backend.model.MenuItem;
import com.quickbite.quickbite_backend.model.Restaurant;
import com.quickbite.quickbite_backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;

    // Get all menu items for a restaurant
    public List<MenuItem> getMenuByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    // Get single menu item
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found!"));
    }

    // Search food items by name
    public List<MenuItem> searchMenuItems(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name);
    }

    // Add new menu item — used by restaurant owner
    public MenuItem addMenuItem(Long restaurantId, MenuItem menuItem) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem);
    }

    // Update menu item — used by restaurant owner
    public MenuItem updateMenuItem(Long id, MenuItem updated) {
        MenuItem existing = getMenuItemById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setAvailable(updated.isAvailable());
        return menuItemRepository.save(existing);
    }

    // Delete menu item
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}