package com.quickbite.quickbite_backend.service;

import com.quickbite.quickbite_backend.model.Restaurant;
import com.quickbite.quickbite_backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // Get all restaurants — used on home page
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // Get only open restaurants
    public List<Restaurant> getOpenRestaurants() {
        return restaurantRepository.findByIsOpenTrue();
    }

    // Get restaurant by ID
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found!"));
    }

    // Search by name — used in search bar
    public List<Restaurant> searchByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    // Filter by cuisine — used in cuisine chips
    public List<Restaurant> getByCuisine(String cuisine) {
        return restaurantRepository.findByCuisine(cuisine);
    }

    // Get restaurants owned by a specific owner
    public List<Restaurant> getByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }

    // Add new restaurant
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Update restaurant details
    public Restaurant updateRestaurant(Long id, Restaurant updated) {
        Restaurant existing = getRestaurantById(id);
        existing.setName(updated.getName());
        existing.setCuisine(updated.getCuisine());
        existing.setAddress(updated.getAddress());
        existing.setPhone(updated.getPhone());
        existing.setOpen(updated.isOpen());
        return restaurantRepository.save(existing);
    }

    // Delete restaurant — used by admin
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}