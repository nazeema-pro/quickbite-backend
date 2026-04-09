package com.quickbite.quickbite_backend.repository;

import com.quickbite.quickbite_backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Find all restaurants by cuisine type
    // Used when user filters by "Pizza" or "Asian"
    List<Restaurant> findByCuisine(String cuisine);

    // Search restaurants by name
    // Used when user types in the search bar
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // Find all restaurants owned by a specific owner
    // Used in restaurant owner dashboard
    List<Restaurant> findByOwnerId(Long ownerId);

    // Find all open restaurants
    // Used to show only available restaurants
    List<Restaurant> findByIsOpenTrue();
}