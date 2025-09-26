package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.PreConditionFailedException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.*;
import com.example.backend.repositories.ProjectRepository;
import com.example.backend.repositories.TeamRepository;

import com.example.backend.repositories.WarehouseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;


    @Autowired
    private TeamRepository teamRepository;


    @GetMapping("")
    public List<Project> getProjects(){
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable int id){
        Project project = projectRepository.findById(id);
        if (project == null){
            throw new ResourceNotFoundException("Project " + id + " not found");
        }
        return project;
    }

    @PostMapping("")
    ResponseEntity<Project> addProject(@RequestBody Project project) {
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(project.getTeamId() + "IWATUWAHFJAWNFAWRJGAJWFKDWAKDAWKGJAWKFAWKDJAWWJAWKFA");
        Team team = teamRepository.findById(project.getTeamId());
        Warehouse warehouse = warehouseRepository.findById(team.getWarehouseId());
        project.setWarehouse(warehouse);
        project.setTeam(team);
        project.setOrders(null);
        Project savedProject = projectRepository.save(project);

        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProject.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedProject);
    }

    @PostMapping("/team/{teamId}")
    public Project addWithTeam(@RequestBody Project project, @PathVariable(required = false) Integer teamId) {
        if (teamId != null) {
            Team foundTeam = teamRepository.findById(teamId);
            project.setTeam(foundTeam);
            Warehouse warehouse = warehouseRepository.findById(foundTeam.getWarehouseId());
            project.setWarehouse(warehouse);
            project.setStatus(Project.Status.UNSTARTED);
        }

        return projectRepository.save(project);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public Project updateProject(@RequestBody JsonNode data, @PathVariable int id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();  // Use Jackson's default ObjectMapper
        Project project = objectMapper.treeToValue(data.get("project"), Project.class);
        int teamId = data.get("teamId").asInt();
        Project existingProject = projectRepository.findById(id);
        assignTeamAndWarehouse(project, teamId);

        if (existingProject == null) {
            throw new NotFoundException(); // Assuming NotFoundException handles cabin not found scenario
        }

        if (project.getId() != id) {
            throw new PreConditionFailedException("Project ID in path does not match ID in request body");
        }

        return projectRepository.save(project);
    }

    public void assignTeamAndWarehouse(Project project, int teamId){
        Team foundTeam = teamRepository.findById(teamId);
        project.setTeam(foundTeam);
        Warehouse warehouse = warehouseRepository.findById(foundTeam.getWarehouseId());
        project.setWarehouse(warehouse);
    }

    @PostMapping("/{projectId}/team/{teamId}")
    public Project assignTeam(@PathVariable int projectId, @PathVariable int teamId){
        System.out.println("testtttttttttttttttttttttttttttttttt");
        Project foundProject = projectRepository.findById(projectId);
        Team foundTeam = teamRepository.findById(teamId);
        System.out.println(foundTeam);
        System.out.println(teamId);
        if (foundProject == null){
            throw new ResourceNotFoundException("Project " + projectId + " not found");
        }
        if (foundTeam == null){
            throw new ResourceNotFoundException("Team " + teamId + " not found");
        }
        return projectRepository.assignTeam(foundProject, teamId);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public void deleteProject(@PathVariable int id){
        Project deletedProject = projectRepository.findById(id);
        if (deletedProject == null){
            throw  new ResourceNotFoundException("Warehouse " + id + " not found");
        }
        projectRepository.deleteById(id);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Project project = new Project();
//        project.setName("Project1");
//        project.setLocation("test");
//        project.setDescription("Lorem ipsum");
//        project.setStatus(Project.Status.ONGOING);
//        projectRepository.save(project);
//    }
}
