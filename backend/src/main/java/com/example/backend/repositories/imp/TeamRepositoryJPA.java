package com.example.backend.repositories.imp;

import com.example.backend.models.User;
import com.example.backend.models.Project;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.repositories.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
@Primary
public class TeamRepositoryJPA implements TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final WarehouseRepository warehouseRepository;

    private final UserRepository userRepository;

    public TeamRepositoryJPA(WarehouseRepository warehouseRepository, UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Team> findAll() {
        TypedQuery<Team> query =
                this.entityManager.createQuery(
                        "select t from Team t", Team.class);
        return query.getResultList();
    }

    @Override
    public Team findById(long id) {
        return entityManager.find(Team.class, id);
    }

    @Override
    public Team save(Team team) {
        if (team.getId() == 0) {
            entityManager.persist(team);
        } else {
            team = entityManager.merge(team);
        }
        return team;
    }

    @Override
    public void deleteById(long id) {
        Team foundTeam = entityManager.find(Team.class, id);

        // Check if the team is associated with a warehouse
        if (foundTeam.getWarehouseId() != null) {
            Warehouse foundWarehouse = warehouseRepository.findById(foundTeam.getWarehouseId());
            removeWarehouse(foundTeam, foundTeam.getWarehouseId());
            List<Project> projects = foundWarehouse.getProjects();
            projects.removeIf(project -> project.getTeamId() == id);
            foundWarehouse.setProjects(projects);
            userRepository.findAll()
                    .stream()
                    .filter(user -> user.getTeamId() == id)
                    .forEach(userRepository::unassignTeam);
        }
        // Remove the team from the warehouse
        entityManager.remove(foundTeam);
    }


    @Override
    public Team assignWarehouse(Team team, int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse != null) {
            team.setWarehouseId(warehouse);
            List<Team> warehouseTeams = warehouse.getTeams();
            warehouseTeams.add(team);
            warehouse.setTeams(warehouseTeams);
            entityManager.merge(warehouse);
            entityManager.merge(team);
        }
        return team;
    }

    @Override
    public Team removeWarehouse(Team team, int warehouseId) {
        team.setWarehouseId(null);
        warehouseRepository.removeTeam(warehouseId, team);
        return team;
    }

    @Override
    public Team assignMember(Team team, User user) {
        if (user != null) {
            List<User> users = team.getMembers();
            users.add(user);
            team.setMembers(users);
            user.setTeamId(team);
            entityManager.merge(team);
            entityManager.merge(user);
        }
        return team;
    }

    @Override
    public Team buildDummyTeam() {
        return null;
    }
}
