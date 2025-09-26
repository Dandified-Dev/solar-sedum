package com.example.backend.repositories.imp;

import com.example.backend.models.Orders;
import com.example.backend.models.Project;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.OrdersRepository;
import com.example.backend.repositories.ProjectRepository;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@Primary
public class ProjectRepositoryJPA implements ProjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private TeamRepository teamRepository;
    private WarehouseRepository warehouseRepository;
    private OrdersRepository ordersRepository;

    public ProjectRepositoryJPA(TeamRepository teamRepository, WarehouseRepository warehouseRepository, OrdersRepository ordersRepository){
        this.teamRepository = teamRepository;
        this.warehouseRepository = warehouseRepository;
        this.ordersRepository = ordersRepository;
    }


    @Override
    public List<Project> findAll() {
        TypedQuery<Project> query =
                this.entityManager.createQuery(
                        "select p from Project p", Project.class);
        return query.getResultList();
    }

    @Override
    public Project findById(long id) {
        return entityManager.find(Project.class, id);
    }

    @Override
    public Project save(Project project) {
        if (project.getId() == 0) {
            entityManager.persist(project);
        } else {
            project = entityManager.merge(project);
        }
        return project;
    }

    @Override
    public void deleteById(long id) {
        Project project = findById(id);
        List<Orders> orders = project.getOrders();
        if (orders != null) {
            for (Orders order : orders) {
                ordersRepository.deleteOrder(order.getId());
            }
        }

        entityManager.remove(project);
    }

    @Override
    public Project assignTeam(Project project, int teamId) {
        Team team = teamRepository.findById(teamId); // Assuming you have a TeamRepository

        if (team != null) {
            project.setTeamId(team);

            // Initialize and add the project to the team's projects list
            List<Project> teamProjects = new ArrayList<>(team.getProjects());
            teamProjects.add(project);
            team.setProjects(teamProjects);

            Warehouse warehouse = warehouseRepository.findById(team.getWarehouseId()); // Assuming you have a WarehouseRepository

            // Check for null before updating warehouse projects
            if (warehouse != null) {
                List<Project> warehouseProjects = new ArrayList<>(warehouse.getProjects());
                warehouseProjects.add(project);
                warehouse.setProjects(warehouseProjects);
            }

            entityManager.merge(team); // Update the team in the database
            entityManager.merge(warehouse); // Update the warehouse in the database
        }

        return project;
    }


    @Override
    public Project buildDummyProject() {
        return null;
    }
}
