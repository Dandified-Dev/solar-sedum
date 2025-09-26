package com.example.backend.controllers;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.JWTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for handling user authentication requests.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTokenUtils jwtUtils;

    /**
     * Authenticates a user based on the provided credentials (email and password).
     *
     * @param loginUser The user credentials (email and password) provided in the request body.
     * @return ResponseEntity containing authentication status, token, user details, and HTTP status.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody User loginUser) {
        // Used to construct the response to be sent back.
        Map<String, Object> response = new HashMap<>();

        try {
            // Attempt to authenticate the user based on provided credentials
            User user = userRepository.findByEmail(loginUser.getEmail());

            if (user != null && user.getPassword().equals(loginUser.getPassword())) {
                // If authentication is successful
                String token = jwtUtils.encode(user.getEmail(), user.getRole());

                response.put("success", true);
                response.put("message", "Login successful!");
                response.put("token", token);
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("role", user.getRole());

                // Return an accepted response with the token in the Authorization header
                return ResponseEntity.accepted()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(response);
            } else {
                // If authentication fails (invalid email or password)
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            // If an unexpected error occurs during authentication
            response.put("success", false);
            response.put("message", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
