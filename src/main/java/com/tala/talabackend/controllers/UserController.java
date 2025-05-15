package com.tala.talabackend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tala.talabackend.models.User;
import com.tala.talabackend.repositories.UserRepository;
import com.tala.talabackend.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService; // inject CustomUserDetailsService

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken.";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully.";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent() &&
            passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(userDetails);
            return token;
        }

        return "Invalid username or password";
    }
}
