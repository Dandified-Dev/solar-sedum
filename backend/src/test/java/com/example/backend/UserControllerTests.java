package com.example.backend;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

import com.example.backend.models.Team;
import com.example.backend.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    private final int userId = 99;
    private final int teamId = 31;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void allUsersCanBeRetrieved() {
        // Arrange
        // No specific arrangement needed for this test

        // Act
        ResponseEntity<User[]> response = this.restTemplate.getForEntity("/users", User[].class);
        User[] users = response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(users.length, is(greaterThan(0)));
    }

    @Test
    public void aNewUserCanBeAdded() {
        // Arrange
        // Retrieve an existing team (assuming team with ID 31 exists)
        ResponseEntity<Team> teamResponse = this.restTemplate.getForEntity("/teams/" + teamId, Team.class);
        Team team = teamResponse.getBody();

        // Create a new user
        User user = new User(userId, "Dannièl", "Herlaar", "test@email.com", "secret123", "user", team);

        // Act
        ResponseEntity<User> response2 = this.restTemplate.postForEntity("/users", user, User.class);
        User savedUser = response2.getBody();

        // Assert
        assertThat(savedUser.getId(), is(greaterThan(0)));
        assertEquals(savedUser.getFirstName(), user.getFirstName());

        // Act
        ResponseEntity<User> response3 = this.restTemplate.getForEntity("/users/" + savedUser.getId(), User.class);
        User retrievedUser = response3.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(retrievedUser);
        assertEquals(retrievedUser.getFirstName(), user.getFirstName());

        // Act
        Team savedTeam = this.restTemplate.getForEntity("/teams/" + team.getId(), Team.class).getBody();

        // Assert
        assertTrue(savedTeam.getMembers().contains(savedUser));
    }

    @Test
    public void deleteUser() {
        // Arrange
        ResponseEntity<User> responseUser = this.restTemplate.getForEntity("/users/" + userId, User.class);
        ResponseEntity<User[]> responseUsers = this.restTemplate.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, responseUser.getStatusCode());
        assertEquals(HttpStatus.OK, responseUsers.getStatusCode());

        User user = responseUser.getBody();

        // Act
        this.restTemplate.delete("/users/" + user.getId());

        Team savedTeam = this.restTemplate.getForEntity("/teams/" + teamId, Team.class).getBody();

        // Assert
        assertFalse(savedTeam.getMembers().contains(user));

        // Assert
        ResponseEntity<User[]> responseUsersAfterDeletion = this.restTemplate.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, responseUsersAfterDeletion.getStatusCode());

        User[] usersAfterDeletion = responseUsersAfterDeletion.getBody();

        boolean userPresentAfterDeletion = false;
        for (User u : usersAfterDeletion) {
            if (u.getId() == user.getId()) {
                userPresentAfterDeletion = true;
                break;
            }
        }

        assertFalse(userPresentAfterDeletion, "User is still present in the list of users after deletion");
    }

    @Test
    public void findUserNotFound() {
        // Arrange
        // No specific arrangement needed for this test

        // Act
        ResponseEntity<User> user = this.restTemplate.getForEntity("/users/" + userId, User.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, user.getStatusCode());
    }
}
