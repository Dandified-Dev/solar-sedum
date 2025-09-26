package com.example.backend.repositories;

import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;

import java.util.List;

public interface WarehouseRepository {
    List<Warehouse> findAll();
    Warehouse findById(long id);
    Warehouse save(Warehouse warehouse);
    Warehouse update(Warehouse warehouse);
    void deleteById(long id);
    void removeTeam(int id, Team team);
    Team findTeam(int id, int teamId);
    Warehouse buildDummyWarehouse();
}
