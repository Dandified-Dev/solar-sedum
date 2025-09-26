package com.example.backend;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmailRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testFindByEmail() {
        // Arrange
        String testEmail = "test@example.com";

        // Act
        User foundUser = userRepository.findByEmail(testEmail);

        // Assert

        // Controls if the found user equals with the expected user
        assertNotNull(foundUser);
        assertEquals(testEmail, foundUser.getEmail());
    }


}
