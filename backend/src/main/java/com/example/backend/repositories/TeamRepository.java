package com.example.backend.repositories;

import com.example.backend.models.User;
import com.example.backend.models.Team;

import java.util.List;

public interface TeamRepository {
    List<Team> findAll();
    Team findById(long id);
    Team save(Team team);
    void deleteById(long id);
    Team assignWarehouse(Team team, int warehouseId);
    Team removeWarehouse(Team team, int warehouseId);
    Team assignMember(Team team, User user);
    Team buildDummyTeam();
}
