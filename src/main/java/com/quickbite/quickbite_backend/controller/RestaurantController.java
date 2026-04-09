package com.quickbite.quickbite_backend.controller;

import com.quickbite.quickbite_backend.model.Restaurant;
import com.quickbite.quickbite_backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // GET ALL RESTAURANTS
    // Frontend calls: GET http://localhost:8080/api/restaurants
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    // GET SINGLE RESTAURANT
    // Frontend calls: GET http://localhost:8080/api/restaurants/1
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    // SEARCH RESTAURANTS
    // Frontend calls: GET http://localhost:8080/api/restaurants/search?name=pizza
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> search(@RequestParam String name) {
        return ResponseEntity.ok(restaurantService.searchByName(name));
    }

    // FILTER BY CUISINE
    // Frontend calls: GET http://localhost:8080/api/restaurants/cuisine/Italian
    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<Restaurant>> getByCuisine(
            @PathVariable String cuisine) {
        return ResponseEntity.ok(restaurantService.getByCuisine(cuisine));
    }

    // ADD RESTAURANT
    // Frontend calls: POST http://localhost:8080/api/restaurants
    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(
            @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
    }

    // UPDATE RESTAURANT
    // Frontend calls: PUT http://localhost:8080/api/restaurants/1
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, restaurant));
    }

    // DELETE RESTAURANT
    // Frontend calls: DELETE http://localhost:8080/api/restaurants/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }
}