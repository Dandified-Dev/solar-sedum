package com.example.backend;

import com.example.backend.models.Team;
import com.example.backend.repositories.imp.TeamRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TeamsRepositoryTests {

    @Autowired
    private TeamRepositoryJPA teamsRepositoryJPA;

    @Test
    public void addReadUpdateTest() {
        // Arrange
        Team team = new Team("Team1", null, null, null);

        // Act
        Team savedTeam = teamsRepositoryJPA.save(team);

        // Assert
        assertTrue(savedTeam.getId() > 0);
        assertEquals("Team1", savedTeam.getName());

        // Act
        savedTeam.setName("Team2");
        Team updatedTeam = teamsRepositoryJPA.save(savedTeam);

        // Assert
        assertEquals(updatedTeam.getName(), savedTeam.getName());

        // Act
        Team retrievedTeam = teamsRepositoryJPA.findById(savedTeam.getId());

        // Assert
        assertEquals(retrievedTeam.getId(), savedTeam.getId());
        assertEquals(retrievedTeam.getName(), savedTeam.getName());
    }

}
