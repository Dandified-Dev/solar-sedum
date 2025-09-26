package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.PreConditionFailedException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.User;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.UserRepository;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.WarehouseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamsController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<Team> getTeams(){
        return teamRepository.findAll();
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable int id){
        Team team = teamRepository.findById(id);
        if (team == null){
            throw new ResourceNotFoundException("Team " + id + " not found");
        }
        return team;
    }

    @PostMapping("/{warehouseId}")
    ResponseEntity<Team> addTeam(@RequestBody Team team, @PathVariable int warehouseId) {
        Team savedTeam = teamRepository.save(team);
        teamRepository.assignWarehouse(team, warehouseId);

        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTeam.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedTeam);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public Team updateTeam(@RequestBody Team team, @PathVariable int id) {
        Team existingTeam = teamRepository.findById(id);

        if (existingTeam == null) {
            throw new NotFoundException(); // Assuming NotFoundException handles cabin not found scenario
        }

        if (team.getId() != id) {
            throw new PreConditionFailedException("Team ID in path does not match ID in request body");
        }

        return teamRepository.save(team);
    }

    @PostMapping("/{teamId}/warehouse/{warehouseId}")
    public Team assignToWarehouse(@PathVariable int teamId, @PathVariable int warehouseId){
        Team foundTeam = teamRepository.findById(teamId);
        Warehouse foundWarehouse = warehouseRepository.findById(warehouseId);
        if (foundTeam == null){
            throw new ResourceNotFoundException("Team " + teamId + " not found");
        }
        if (foundWarehouse == null){
            throw new ResourceNotFoundException("Warehouse " + warehouseId + " not found");
        }
        return teamRepository.assignWarehouse(foundTeam, warehouseId);
    }

    @PostMapping("/{teamId}/addMember/{memberId}")
    ResponseEntity<Team> addMember(@PathVariable int teamId, @PathVariable int memberId) {
        Team foundTeam = teamRepository.findById(teamId);
        User foundUser = userRepository.findById(memberId);
        if (foundTeam == null){
            throw new ResourceNotFoundException("Team " + teamId + " not found");
        }
        Team savedMember = teamRepository.assignMember(foundTeam, foundUser);
        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{member}")
                .buildAndExpand(savedMember)
                .toUri();

        return ResponseEntity.created(location).body(savedMember);
    }


    @DeleteMapping(path = "{id}", produces = "application/json")
    public void deleteTeam(@PathVariable int id){
        Team deletedTeam = teamRepository.findById(id);
        if (deletedTeam == null){
            throw  new ResourceNotFoundException("Team " + id + " not found");
        }
        teamRepository.deleteById(id);
    }

    @PutMapping("/{teamId}/warehouse/remove/{warehouseId}")
    public Team removeWarehouse(@PathVariable int teamId, @PathVariable int warehouseId){
        Team foundTeam = teamRepository.findById(teamId);
        return teamRepository.removeWarehouse(foundTeam, warehouseId);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Team team1 = new Team();
//        team1.setName("Team1284");
//
//        teamRepository.save(team1);
//
//    }
}
