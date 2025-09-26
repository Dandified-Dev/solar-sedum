package com.example.backend.repositories;

import com.example.backend.models.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findById(long id);
    User save(User user);
    void deleteById(long id);
    User unassignTeam(User user);
//    Member assignTeam(Member member, int teamId);
    User findByEmail(String email);
}
