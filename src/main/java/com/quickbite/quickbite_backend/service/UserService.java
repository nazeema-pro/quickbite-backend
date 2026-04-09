package com.quickbite.quickbite_backend.service;

import com.quickbite.quickbite_backend.model.User;
import com.quickbite.quickbite_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }
        // Encrypt the password before saving
        // Never store plain text passwords
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get user by email — used during login
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    // Get all users — used by admin
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user profile
    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setName(updatedUser.getName());
        existing.setPhone(updatedUser.getPhone());
        return userRepository.save(existing);
    }

    // Delete user — used by admin
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}