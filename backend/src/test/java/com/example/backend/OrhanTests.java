package com.example.backend;

import com.example.backend.controllers.AuthenticationController;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.JWTokenInfo;
import com.example.backend.security.JWTokenUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = BackendApplication.class)
public class OrhanTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTokenUtils jwtUtils;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Autowired
    private JWTokenUtils jwtUtilss;

    @Test
    public void testEncodeAndDecode() {
        // Arrange
        String testEmail = "test@example.com";
        String testRole = "user";

        // Act
        String token = jwtUtilss.encode(testEmail, testRole);

        // Assert
        assertNotNull(token);

        // Decodeer het token om de claims te verkrijgen
        JWTokenInfo jwTokenInfo = JWTokenUtils.decode(token, jwtUtilss.getPassphrase());

        // Verifieer de claims
        assertEquals(testEmail, jwTokenInfo.getEmail());
    }

    @Test
    public void testSuccessfulAuthentication() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setId(1);
        user.setRole("user");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(jwtUtils.encode("test@example.com", "user")).thenReturn("fakeToken");

        // Act
        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(user);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Login successful!", response.getBody().get("message"));
        assertEquals("fakeToken", response.getBody().get("token"));
        assertEquals(1, response.getBody().get("userId"));
        assertEquals("test@example.com", response.getBody().get("email"));
        assertEquals("user", response.getBody().get("role"));

        // Verify that userRepository.findByEmail and jwtUtils.encode were called
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtUtils, times(1)).encode("test@example.com", "user");
    }

    @Test
    public void testFailedAuthentication() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act
        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(user);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, response.getBody().get("success"));
        assertEquals("Invalid email or password", response.getBody().get("message"));

        // Verify that userRepository.findByEmail was called
        verify(userRepository, times(1)).findByEmail("test@example.com");
        // Verify that jwtUtils.encode was not called
        verify(jwtUtils, never()).encode(any(), any());
    }

    @Test
    public void testUnexpectedError() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Map<String, Object>> response = authenticationController.authenticate(user);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().get("success"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));

        // Verify that userRepository.findByEmail was called
        verify(userRepository, times(1)).findByEmail("test@example.com");
        // Verify that jwtUtils.encode was not called
        verify(jwtUtils, never()).encode(any(), any());
    }







}
