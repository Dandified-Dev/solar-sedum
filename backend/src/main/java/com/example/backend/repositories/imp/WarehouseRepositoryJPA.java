package com.example.backend.repositories.imp;

import com.example.backend.models.Project;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Primary
@Transactional
@Repository
public class WarehouseRepositoryJPA implements WarehouseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Warehouse> findAll() {
        TypedQuery<Warehouse> query =
                this.entityManager.createQuery(
                        "select w from Warehouse w", Warehouse.class);
        return query.getResultList();
    }

    @Override
    public Warehouse findById(long id) {
        return entityManager.find(Warehouse.class, id);
    }

    @Override
    @Transactional
    public Warehouse save(Warehouse warehouse) {
        if (warehouse.getId() == 0) {
            entityManager.persist(warehouse);
        } else {
            warehouse = entityManager.merge(warehouse);
        }
        return warehouse;
    }

    @Override
    @Transactional
    public Warehouse update(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }

        Warehouse foundWarehouse = entityManager.find(Warehouse.class, warehouse.getId());

        if (foundWarehouse != null) {
            foundWarehouse.setLocation(warehouse.getLocation());
            entityManager.merge(foundWarehouse);

            return foundWarehouse;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Warehouse warehouse = entityManager.find(Warehouse.class, id);
        List<Team> teams = warehouse.getTeams();
        if (warehouse.getTeams() != null) {
            for (Team team : teams){
                team.setWarehouseId(null);
                entityManager.merge(team);
            }
            teams.clear();
        }
        List<Project> projects = warehouse.getProjects();
        if (warehouse.getProjects() != null) {
            for (Project project : projects){
                project.setWarehouse(null);
                entityManager.merge(project);
            }
        }
        entityManager.remove(warehouse);
    }

    @Override
    @Transactional
    public void removeTeam(int id, Team team) {
        Warehouse foundWarehouse = findById(id);
        if (foundWarehouse != null) {
            List<Team> teams = foundWarehouse.getTeams();
            if (teams.contains(team)) {
                updateTeamsAndWarehouses(team);
                teams.remove(team);
                foundWarehouse.setTeams(teams);
                team.setWarehouseId(null);
                entityManager.merge(team);
                entityManager.merge(foundWarehouse);
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
            entityManager.merge(warehouse);
        });
        entityManager.merge(team);
    }

    @Override
    public Team findTeam(int warehouseId, int teamId) {
        Warehouse foundWarehouse = entityManager.find(Warehouse.class, warehouseId);

        if (foundWarehouse == null) {
            return null; // Warehouse not found
        }

        List<Team> teams = foundWarehouse.getTeams();

        for (Team team : teams) {
            if (team.getId() == teamId) {
                return team; // Found the team, return immediately
            }
        }

        return null; // Team not found
    }


    @Override
    public Warehouse buildDummyWarehouse() {
        return null;
    }
}
