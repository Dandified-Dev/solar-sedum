//package com.example.backend.repositories.imp;
//
//import com.example.backend.models.Material;
//import com.example.backend.models.Project;
//import com.example.backend.models.Team;
//import com.example.backend.models.Warehouse;
//import com.example.backend.repositories.ProjectRepository;
//import com.example.backend.repositories.TeamRepository;
//import com.example.backend.repositories.WarehouseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Repository
//public class ProjectRepositoryMock implements ProjectRepository {
//    private List<Project> internalProjects;
//    private TeamRepository teamRepositoryMock;
//    private WarehouseRepository warehouseRepository;
//    public ProjectRepositoryMock(TeamRepository teamRepositoryMock, WarehouseRepository warehouseRepository){
//        this.teamRepositoryMock = teamRepositoryMock;
//        this.warehouseRepository = warehouseRepository;
//        init();
//    }
//
//
//    private void init(){
//        this.internalProjects = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            internalProjects.add(buildDummyProject());
//        }
//    }
//    @Override
//    @GetMapping()
//    public List<Project> findAll() {
//        return this.internalProjects;
//    }
//
//    @Override
//    public Project findById(long id) {
//        for (Project item : internalProjects){
//            if(item.getId() == id){
//                return item;
//            }
//        }
//        return null;
//    }
//
//    public Project save(Project project) {
//        if (project == null) {
//            // Handle the case where project is null
//            return null; // Or throw an exception
//        }
//
//        Project foundProject = findById(project.getId());
//
//        if (foundProject != null) {
//            internalProjects.removeIf(p -> p.getId() == project.getId());
//        }
//
//        internalProjects.add(project);
//
//        if (project.getTeamId() != null) {
//            updateTeamsAndWarehouses(project);
//            assignTeam(project, project.getTeamId());
//        }
//
//        return project;
//    }
//
//    private void updateTeamsAndWarehouses(Project project) {
//        teamRepositoryMock.findAll().forEach(team -> team.getProjects().removeIf(p -> p.getId() == project.getId()));
//        warehouseRepository.findAll().forEach(warehouse -> warehouse.getProjects().removeIf(p -> p.getId() == project.getId()));
//    }
//
//
//
//    @Override
//    public void deleteById(long id) {
//        Project foundProject = findById(id);
//        if (foundProject != null){
//            internalProjects.remove(foundProject);
//            updateTeamsAndWarehouses(foundProject);
//        }
//    }
//
//    @Override
//    public Project assignTeam(Project project, int teamId) {
//        Team team = teamRepositoryMock.findById(teamId);
//
//        if (team != null) {
//            project.setTeamId(team);
//
//            // Initialize and add the project to the team's projects list
//            List<Project> teamProjects = new ArrayList<>(team.getProjects());
//            teamProjects.add(project);
//            team.setProjects(teamProjects);
//
//            Warehouse warehouse = warehouseRepository.findById(team.getWarehouseId());
//
//            // Check for null before updating warehouse projects
//            if (warehouse != null) {
//                List<Project> warehouseProjects = new ArrayList<>(warehouse.getProjects());
//                warehouseProjects.add(project);
//                warehouse.setProjects(warehouseProjects);
//            }
//        }
//        return project;
//    }
//
//
//    public static int randomNumber(){
//        return (int) Math.floor(Math.random() * (100000 - 1 + 1));
//    }
//
//    @Override
//    public Project buildDummyProject() {
//        final int id = (int) Math.floor(Math.random() * (100000 - 1 + 1));
//        final String name = String.format("project %s", randomNumber());
//        final String location = String.format("location %s", randomNumber());
//        final String description = String.format("lorem ipsum");
//        int randomIndex = (int) (Math.random() * teamRepositoryMock.findAll().size());
//        Team randomTeam = teamRepositoryMock.findAll().get(randomIndex);
//
//        final Team teamId = randomTeam;
//        final List<Material> materials = new ArrayList<>();
//        Project project= new Project(id, name, location, description, getRandomStatus(), teamId, materials);
//        assignTeam(project, teamId.getId());
//        return project;
//    }
//
//    private Project.Status getRandomStatus() {
//        int random = (int) (Math.random() * ((3 - 1) + 1));
//        if (random == 1) {
//            return Project.Status.DONE;
//        }
//        if (random == 2) {
//            return Project.Status.ONGOING;
//        }
//        return Project.Status.UNSTARTED;
//    }
//
//}
