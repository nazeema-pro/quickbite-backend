package com.quickbite.quickbite_backend.controller;

import com.quickbite.quickbite_backend.model.MenuItem;
import com.quickbite.quickbite_backend.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MenuItemController {

    private final MenuItemService menuItemService;

    // GET MENU FOR A RESTAURANT
    // Frontend calls: GET http://localhost:8080/api/menu/restaurant/1
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenu(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(
                menuItemService.getMenuByRestaurant(restaurantId));
    }

    // GET SINGLE MENU ITEM
    // Frontend calls: GET http://localhost:8080/api/menu/1
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    // SEARCH FOOD ITEMS
    // Frontend calls: GET http://localhost:8080/api/menu/search?name=burger
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> search(@RequestParam String name) {
        return ResponseEntity.ok(menuItemService.searchMenuItems(name));
    }

    // ADD MENU ITEM
    // Frontend calls: POST http://localhost:8080/api/menu/restaurant/1
    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable Long restaurantId,
            @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(
                menuItemService.addMenuItem(restaurantId, menuItem));
    }

    // UPDATE MENU ITEM
    // Frontend calls: PUT http://localhost:8080/api/menu/1
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable Long id,
            @RequestBody MenuItem menuItem) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItem));
    }

    // DELETE MENU ITEM
    // Frontend calls: DELETE http://localhost:8080/api/menu/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }
}