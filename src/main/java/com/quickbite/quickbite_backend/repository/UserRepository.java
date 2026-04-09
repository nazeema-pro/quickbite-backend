package com.quickbite.quickbite_backend.repository;

import com.quickbite.quickbite_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email address
    // Used during login to check if email exists
    Optional<User> findByEmail(String email);

    // Check if an email is already registered
    // Used during registration to prevent duplicates
    boolean existsByEmail(String email);
}