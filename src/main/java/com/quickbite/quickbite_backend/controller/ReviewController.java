package com.quickbite.quickbite_backend.controller;

import com.quickbite.quickbite_backend.model.Review;
import com.quickbite.quickbite_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    // ADD REVIEW
    // Frontend calls: POST http://localhost:8080/api/reviews
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long restaurantId = Long.valueOf(
                    request.get("restaurantId").toString());
            int rating = Integer.parseInt(request.get("rating").toString());
            String comment = request.get("comment").toString();
            return ResponseEntity.ok(
                    reviewService.addReview(userId, restaurantId, rating, comment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET RESTAURANT REVIEWS
    // Frontend calls: GET http://localhost:8080/api/reviews/restaurant/1
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Review>> getRestaurantReviews(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(
                reviewService.getRestaurantReviews(restaurantId));
    }

    // GET USER REVIEWS
    // Frontend calls: GET http://localhost:8080/api/reviews/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getUserReviews(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }

    // DELETE REVIEW
    // Frontend calls: DELETE http://localhost:8080/api/reviews/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }
}