//package com.example.backend.repositories.imp;
//
//import com.example.backend.models.Member;
//import com.example.backend.models.Project;
//import com.example.backend.models.Team;
//import com.example.backend.models.Warehouse;
//import com.example.backend.repositories.MemberRepository;
//import com.example.backend.repositories.TeamRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Repository
//public class MemberRepositoryMock implements MemberRepository {
//
//    private List<Member> internalMembers;
//
//    private final TeamRepository teamRepository;
//
//    private static final String[] names = {
//            "Alice", "Bob", "Charlie", "David", "Eva",
//            "Frank", "Grace", "Henry", "Ivy", "Jack",
//            "Kate", "Liam", "Mia", "Noah", "Olivia",
//            "Parker", "Quinn", "Ryan", "Sophia", "Thomas",
//            "Ursula", "Victor", "Wendy", "Xander", "Yara",
//            "Zane", "Amelia", "Benjamin", "Chloe", "Daniel",
//            "Emma", "Finn", "Gemma", "Harrison", "Isabella",
//            "James", "Kylie", "Leo", "Mila", "Nathan",
//            "Oscar", "Penelope", "Quincy", "Riley", "Samuel",
//            "Tessa", "Ulysses", "Violet", "Walter", "Xena",
//            "Yasmine", "Zachary", "Ava", "Brayden", "Cora",
//            "Dylan", "Eleanor", "Felix", "Gabriella", "Hazel",
//            "Isaac", "Jasmine", "Kai", "Lila", "Mason",
//            "Nora", "Owen", "Peyton", "Quinn", "Rebecca",
//            "Sebastian", "Tiffany", "Uriel", "Victoria", "William",
//            "Ximena", "Yuki", "Zara", "Aaron", "Bella",
//            "Caleb", "Delilah", "Elijah", "Fiona", "George",
//            "Hannah", "Isla", "Jacob", "Katherine"
//    };
//
//    public MemberRepositoryMock(TeamRepositoryMock teamRepositoryMock){
//        this.teamRepository = teamRepositoryMock;
//        init();
//    }
//
//    private void init(){
//        this.internalMembers = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            internalMembers.add(buildDummyMember());
//
//        }
//    }
//    @Override
//    public List<Member> findAll() {
//        return this.internalMembers;
//    }
//
//    @Override
//    public Member findById(long id) {
//        for (Member item : internalMembers){
//            if(item.getId() == id){
//                return item;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Member save(Member member) {
//        if (member == null) {
//            return null;
//        }
//
//        Integer teamId = member.getTeamId();
//
//        Member foundMember = findById(member.getId());
//
//        if (foundMember != null) {
//            internalMembers.replaceAll(existingMember -> existingMember.getId() == member.getId() ? member : existingMember);
//        } else {
//            internalMembers.add(member);
//        }
//
//        if (teamId != null) {
//            assignTeam(member, member.getTeamId());
//        }
//
//        return member;
//    }
//
//
//    public Member assignTeam(Member member, int teamId) {
//        Team team = teamRepository.findById(teamId);
//
//        if (team != null) {
//            member.setTeamId(team);
//
//            // Check if a member with the same ID already exists in the team
//            boolean memberExists = false;
//            for (int i = 0; i < team.getMembers().size(); i++) {
//                Member existingMember = team.getMembers().get(i);
//                if (existingMember.getId() == member.getId()) {
//                    // Update the existing member
//                    existingMember.setName(member.getName());
//                    memberExists = true;
//                    break;
//                }
//            }
//
//            if (!memberExists) {
//                // Add the member to the team
//                team.getMembers().add(member);
//            }
//        }
//
//        return member;
//    }
//
//
//    @Override
//    public void deleteById(long id) {
//        Member foundMember = findById(id);
//        if (foundMember != null){
//            internalMembers.remove(foundMember);
//            updateTeams(foundMember);
//        }
//    }
//
//    public void updateTeams(Member member){
//        teamRepository.findAll().forEach(team -> team.getMembers().removeIf(m -> m.getId() == member.getId()));
//    }
//
//    public Member buildDummyMember() {
//        final int id = (int) Math.floor(Math.random() * (100 - 1 + 1));
//        final String name = getRandomName();
//        int randomIndex = (int) (Math.random() * teamRepository.findAll().size());
//        Team randomTeam = teamRepository.findAll().get(randomIndex);
//
//        final Team teamId = randomTeam;
//        Member member = new Member(id, name, teamId);
//        assignTeam(member, teamId.getId());
//        return member;
//    }
//
//    private String getRandomName() {
//        Random random = new Random();
//        int index = random.nextInt(names.length);
//        return names[index];
//    }
//}
