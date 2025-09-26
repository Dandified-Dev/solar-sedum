package com.example.backend.models;


import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

@Entity
@Table(name= "User")
public class User {
    @Id
    private int id;
    private String firstName;
    private String lastName;

    private String email;

    private String password;
    private String role;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team teamId;

    public User(int id, String firstName, String lastName, String email, String password, String role, Team teamId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User otherUser = (User) obj;
        return Objects.equals(id, otherUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        // Set the password if provided or generate a random password
        this.password = (password != null && !password.isEmpty()) ? password : generateRandomPassword();
    }

    // Method to generate a random password with a fixed length
    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTeamId() {
        if (teamId != null){
            return teamId.getId();
        }
        return null;
    }

    public void setTeamId(Team teamId) {
        this.teamId = teamId;
    }


}
