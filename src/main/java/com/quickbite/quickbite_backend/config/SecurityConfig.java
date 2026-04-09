package com.quickbite.quickbite_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                // Set session to stateless
                // because we use JWT not sessions
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define which endpoints are public
                // and which require login
                .authorizeHttpRequests(auth -> auth

                        // These URLs are open to everyone
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/restaurants/**").permitAll()
                        .requestMatchers("/api/menu/**").permitAll()

                        // These URLs require login
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/reviews/**").authenticated()

                        // Admin only URLs
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Everything else requires login
                        .anyRequest().authenticated()
                )

                // Add JWT filter before the default
                // Spring Security filter
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Password encoder — uses BCrypt hashing
    // Converts "password123" to
    // "$2a$10$xyzABCencryptedstring"
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}