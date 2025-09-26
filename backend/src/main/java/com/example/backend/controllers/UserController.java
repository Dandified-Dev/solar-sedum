package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.PreConditionFailedException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Team;
import com.example.backend.models.User;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.EmailSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmailSenderService senderService;


    @GetMapping("")
    public List<User> getMembers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getMember(@PathVariable int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("Team " + id + " not found");
        }
        return user;
    }

    @PostMapping("")
    public ResponseEntity<User> addMember(@RequestBody User user) {

        if (user.getPassword() == null){
            user.setPassword(RandomStringUtils.randomAlphanumeric(10));
        }

        if (user.getTeamId() != null){
            Team team = teamRepository.findById(user.getTeamId());
            user.setTeamId(team);
        }

        user.setRole("user");
        User savedUser = userRepository.save(user);

        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        sendMail(user.getFirstName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(location).body(savedUser);
    }

    @PutMapping(path = "{id}", produces = "application/json")
    public User updateMember(@RequestBody JsonNode data, @PathVariable int id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();  // Use Jackson's default ObjectMapper

        User user = objectMapper.treeToValue(data.get("user"), User.class);
        Integer teamId = data.get("teamId").asInt();
        User existingUser = userRepository.findById(id);

        if (existingUser == null) {
            throw new NotFoundException(); // Assuming NotFoundException handles cabin not found scenario
        }

        if (user.getId() != id) {
            throw new PreConditionFailedException("Member ID in path does not match ID in request body");
        }
        System.out.println(teamId);
        Team team = teamRepository.findById(teamId);
        user.setTeamId(team);
        user.setRole("user");
        user.setPassword(userRepository.findById(user.getId()).getPassword());

        return userRepository.save(user);
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public void deleteMember(@PathVariable int id) {
        User deletedUser = userRepository.findById(id);
        if (deletedUser == null) {
            throw new ResourceNotFoundException("Member " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @PutMapping(path = "/{id}/unassignTeam", produces = "application/json")
    public User unassignTeam(@PathVariable int id){
        System.out.println("ODAWIGIWAKDWAKDAKWGAWIDA");
        User existingUser = userRepository.findById(id);

        if (existingUser == null) {
            throw new NotFoundException(); // Assuming NotFoundException handles cabin not found scenario
        }

        return userRepository.unassignTeam(existingUser);
    }

    @GetMapping("/checkEmail/{email}")
    public boolean isEmailAvailable(@PathVariable String email){
        List<User> users = userRepository.findAll();
        boolean isEmailTaken = users.stream().anyMatch(user -> user.getEmail().equals(email));
        return !isEmailTaken;
    }

    @GetMapping("/getUser/{email}")
    public User findByEmail(@PathVariable String email) {
        if(email != null){
            User user = userRepository.findByEmail(email);
            return user;
        }
        return null;
    }

    public void sendMail(String firstName,String email, String password){
        senderService.sendEmail(email, "Welcome to Solar Sedum - Your New Employee Account Information", String.format("Dear %s,\n", firstName) +
                "\n" +
                "We are delighted to welcome you to the Solar Sedum team! We are confident that your skills and expertise will contribute significantly to our continued success.\n" +
                "\n" +
                "To ensure a smooth onboarding process, we have set up your employee account, and you can now access our internal systems using the following credentials:\n" +
                "\n" +
                String.format("Email: %s\n", email) +
                String.format( "Password: %s\n", password) +
                "\n" +
                "Please follow the steps below to complete the login process:\n" +
                "\n" +
                "Go to the [Company Intranet Portal]: [IntranetURL]\n" +
                "\n" +
                "Enter your Email and Temporary Password: Use the provided email address and password to log in.\n" +
                "\n" +
                "If you encounter any issues during the login process or have any questions, feel free to reach out to our IT support team at [ITSupport@company.com].\n" +
                "\n" +
                "Once again, welcome to the [Company Name] family! We look forward to achieving great success together.\n" +
                "\n" +
                "Best regards,\n" +
                "\n" +
                "Dannièl Herlaar\n" +
                "IT DEV\n" +
                "Solar Sedum\n" +
                "dannielherlaar@gmail.com\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n");
    }


//    @Override
//    public void run(String... args) throws Exception {
//        Member member1 = new Member();
//        member1.setName("David");
//        member1.setEmail("davidduivenvoorden@gmail.com");
//        member1.setPassword("Hoi");
//        member1.setRole("admin");
//
//        Member member2 = new Member();
//        member1.setName("Kees");
//        member1.setEmail("Test12848@gmail.com");
//        member1.setPassword("kop");
//        member1.setRole("user");
//
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//
//    }
}
