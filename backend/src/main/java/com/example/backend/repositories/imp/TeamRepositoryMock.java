//package com.example.backend.repositories.imp;
//
//import com.example.backend.models.Member;
//import com.example.backend.models.Project;
//import com.example.backend.models.Team;
//import com.example.backend.models.Warehouse;
//import com.example.backend.repositories.TeamRepository;
//import com.example.backend.repositories.WarehouseRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class TeamRepositoryMock implements TeamRepository {
//    private List<Team> internalTeams;
//    private final WarehouseRepository warehouseRepositoryMock;
//
//    public TeamRepositoryMock(WarehouseRepository warehouseRepository){
//        this.warehouseRepositoryMock = warehouseRepository;
//        init();
//    }
//    private void init(){
//        this.internalTeams = new ArrayList<>();
//        for (int i = 0; i < 18; i++) {
//            internalTeams.add(buildDummyTeam());
//        }
//    }
//
//    @Override
//    public List<Team> findAll() {
//        return this.internalTeams;
//    }
//
//    @Override
//    public Team findById(long id) {
//        for (Team item : internalTeams) {
//            if(item.getId() == id){
//                return item;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Team save(Team team) {
//        if (team == null) {
//            // Handle the case when the provided team is null
//            return null;
//        }
//
//        Integer warehouseId = team.getWarehouseId();
//
//        Team foundTeam = findById(team.getId());
//
//        if (foundTeam != null) {
//            for (int i = 0; i < internalTeams.size(); i++) {
//                if (internalTeams.get(i).getId() == team.getId()) {
//                    internalTeams.set(i, team);
//                    if (warehouseId != null) {
//                        assignWarehouse(team, warehouseId);
//                    }
//                    return team; // Return the updated team here
//                }
//            }
//        } else {
//            internalTeams.add(team);
//            if (warehouseId != null) {
//                assignWarehouse(team, warehouseId);
//            }
//            return team; // Return the newly added team here
//        }
//
//        return null; // If no team is updated or added
//    }
//
//    @Override
//    public void deleteById(long id) {
//        Team foundTeam = findById(id);
//        if (foundTeam.getWarehouseId() != null){
//            Warehouse foundWarehouse = warehouseRepositoryMock.findById(foundTeam.getWarehouseId());
//            removeWarehouse(foundTeam, foundTeam.getWarehouseId());
//            List<Project> projects = foundWarehouse.getProjects();
//            projects.removeIf(project -> project.getTeamId() == id);
//            foundWarehouse.setProjects(projects);
//        }
//        internalTeams.remove(foundTeam);
//
//    }
//
//    @Override
//    public Team assignWarehouse(Team team, int warehouseId){
//        Warehouse warehouse = warehouseRepositoryMock.findById(warehouseId);
//        if (warehouse != null) {
//            team.setWarehouseId(warehouse);
//            List<Team> warehouseTeams = warehouse.getTeams();
//            warehouseTeams.add(team);
//            warehouse.setTeams(warehouseTeams);
//        }
//        return team;
//    }
//
//    @Override
//    public Team removeWarehouse(Team team, int warehouseId) {
//        team.setWarehouseId(null);
//        warehouseRepositoryMock.removeTeam(warehouseId, team);
//        return team;
//    }
//
//    @Override
//    public Team assignMember(Team team, Member member) {
//        if (member != null){
//             List<Member> members = team.getMembers();
//             members.add(member);
//             team.setMembers(members);
//        }
//        return team;
//    }
//
//
//    public static int randomNumber(){
//        return (int) Math.floor(Math.random() * (100000 - 1 + 1));
//    }
//    @Override
//    public Team buildDummyTeam() {
//        final int id = randomNumber();
//        final String name = String.format("Team %s", randomNumber());
//
//        int randomIndex = (int) (Math.random() * warehouseRepositoryMock.findAll().size());
//        Warehouse randomWarehouse = warehouseRepositoryMock.findAll().get(randomIndex);
//        Warehouse warehouseId = randomWarehouse;
//
//        final List members = new ArrayList<>();
//        final List<Project> projects = new ArrayList<>();
//        Team team = new Team(id, name, warehouseId, members, projects);
//        assignWarehouse(team, warehouseId.getId());
//        return team;
//    }
//}
