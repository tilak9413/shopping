package org.example.shopping.controller;
import lombok.RequiredArgsConstructor;
import org.example.shopping.config.JwtUtil;
import org.example.shopping.model.LoginRequest;
import org.example.shopping.model.User;
import org.example.shopping.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent() &&
                passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            String token = jwtUtil.generateToken(userOptional.get().getUsername());
            response.put("message", "Login Successful");
            response.put("token", token);
            return response;
        } else {
            response.put("message", "Invalid Credentials");
            return response;
        }
    }
}