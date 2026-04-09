package com.quickbite.quickbite_backend.service;

import com.quickbite.quickbite_backend.model.Review;
import com.quickbite.quickbite_backend.model.User;
import com.quickbite.quickbite_backend.model.Restaurant;
import com.quickbite.quickbite_backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    // Add a new review
    public Review addReview(Long userId, Long restaurantId,
                            int rating, String comment) {

        // Check if user already reviewed this restaurant
        if (reviewRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new RuntimeException("You already reviewed this restaurant!");
        }

        User user = userService.getUserById(userId);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    // Get all reviews for a restaurant
    public List<Review> getRestaurantReviews(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    // Get all reviews by a user
    public List<Review> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    // Delete a review
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}