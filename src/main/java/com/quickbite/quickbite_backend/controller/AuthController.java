package com.quickbite.quickbite_backend.controller;

import com.quickbite.quickbite_backend.config.JwtUtil;
import com.quickbite.quickbite_backend.model.User;
import com.quickbite.quickbite_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // LOGIN — returns JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() ->
                            new RuntimeException("Email not found!"));

            // Check if password matches
            if (!passwordEncoder.matches(
                    loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body("Invalid password!");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // Return token and user info
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}