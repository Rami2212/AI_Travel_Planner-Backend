package com.aitravelplanner.user.service;

import com.aitravelplanner.user.model.Role;
import com.aitravelplanner.user.model.User;
import com.aitravelplanner.user.repository.UserRepository;
import com.aitravelplanner.user.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Register new user
    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    // Authenticate user and generate token
    public String authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();
        if (!user.getActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtTokenUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());
    }

    // Get user by ID
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Admin: Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Admin: Toggle user active status
    public User toggleUserStatus(UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }

    // Get active users count
    public long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }

    // Validate token and get user
    public User validateTokenAndGetUser(String token) {
        if (!jwtTokenUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String email = jwtTokenUtil.getEmailFromToken(token);
        return getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}