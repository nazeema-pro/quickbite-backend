package com.quickbite.quickbite_backend.repository;

import com.quickbite.quickbite_backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for a specific restaurant
    // Used on restaurant page to show ratings
    List<Review> findByRestaurantId(Long restaurantId);

    // Get all reviews written by a specific user
    // Used in user profile
    List<Review> findByUserId(Long userId);

    // Check if user already reviewed this restaurant
    // Prevents duplicate reviews
    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
}