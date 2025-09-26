package com.example.backend.repositories.imp;

import com.example.backend.models.Inventory;
import com.example.backend.models.Project;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.WarehouseRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseRepositoryMock implements WarehouseRepository {
    private List<Warehouse> internalWarehouses;
    private List<Inventory> inventory;
    public WarehouseRepositoryMock(){
        init();
    }

    private void init(){
        this.internalWarehouses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            internalWarehouses.add(buildDummyWarehouse());
        }
    }
    @Override
    public List<Warehouse> findAll() {
        return this.internalWarehouses;
    }

    @Override
    public Warehouse findById(long id) {
        for (Warehouse item : internalWarehouses) {
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }


    @Override
    public Warehouse save(Warehouse warehouse) {
        Warehouse foundWarehouse = findById(warehouse.getId());
        if(foundWarehouse != null){
            for (int i = 0; i < internalWarehouses.size(); i++) {
                if (internalWarehouses.get(i).getId() == warehouse.getId()) {
                    internalWarehouses.set(i, warehouse);
                    break;
                }
            }
        }else{
            internalWarehouses.add(warehouse);
            return warehouse;
        }
        return foundWarehouse;
    }

    public Warehouse update(Warehouse warehouse) {
        Warehouse foundWarehouse = findById(warehouse.getId());

        if(foundWarehouse != null) {
            foundWarehouse.setLocation(warehouse.getLocation());
        } else{
            return null;
        }

        return foundWarehouse;
    }

    @Override
    public void deleteById(long id) {
        Warehouse foundWarehouse = findById(id);
        if (foundWarehouse != null){
            internalWarehouses.remove(foundWarehouse);
            List<Team> teams = foundWarehouse.getTeams();
            for (Team team : teams){
                team.setWarehouseId(null);
            }
        }
    }

    @Override
    public void removeTeam(int id, Team team) {
        Warehouse foundWarehouse = findById(id);
        if (foundWarehouse != null) {
            List<Team> teams = foundWarehouse.getTeams();
            if (teams.contains(team)) {
                updateTeamsAndWarehouses(team);
                teams.remove(team);
                foundWarehouse.setTeams(teams);
                team.setWarehouseId(null);
            }
        }
    }

    public void updateTeamsAndWarehouses(Team team) {
        findAll().forEach(warehouse -> {
            List<Project> projectsToRemove = new ArrayList<>();
            warehouse.getProjects().forEach(project -> {
                if (project.getTeamId() == team.getId()) {
                    projectsToRemove.add(project);
                }
            });
            warehouse.getProjects().removeAll(projectsToRemove);
        });
    }

    @Override
    public Team findTeam(int id, int teamId) {
        Warehouse foundWarehouse = findById(id);
        List<Team> teams = foundWarehouse.getTeams();
        Team foundTeam = null;
        for (Team team : teams) {
            if (team.getId() == teamId && foundTeam == null) {
                foundTeam = team;
            }
        }
        return foundTeam;
    }

    public static int randomNumber(){
        return (int) Math.floor(Math.random() * (100000 - 1 + 1));
    }
    @Override
    public Warehouse buildDummyWarehouse() {
        final int id = randomNumber();
        final String name = String.format("Warehouse %s", randomNumber());
        final String location = String.format("Location, %s", randomNumber());
        final List<Team> teams = new ArrayList<>();
        final List<Project> projects = new ArrayList<>();
        final List<Inventory> inventory = new ArrayList<>();

        return new Warehouse(id, name, location, teams, projects, inventory);
    }
}
