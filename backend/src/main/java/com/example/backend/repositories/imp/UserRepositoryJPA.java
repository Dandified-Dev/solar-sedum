package com.example.backend.repositories.imp;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.repositories.TeamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
@Primary
public class UserRepositoryJPA implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public UserRepositoryJPA() {

    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query =
                this.entityManager.createQuery(
                        "select m from User m", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User save(User user) {
        if (user.getId() != 0) {
            // If the user has an ID (not a new entity), use merge for updating
            return entityManager.merge(user);
        } else {
            // If the user is a new entity, use persist for saving
            entityManager.persist(user);
            return user;
        }
    }


    @Override
    public void deleteById(long id) {
        User user = findById(id);
        entityManager.remove(user);
    }

    /**
     * @param user
     * @return
     */
    @Override
    public User unassignTeam(User user) {
        user.setTeamId(null);
        return entityManager.merge(user);
    }

//    @Override
//    public Member assignTeam(Member member, int teamId) {
//        Team team = teamRepository.findById(teamId); // Assuming you have a TeamRepository
//
//        if (team != null) {
//            member.setTeamId(team);
//
//            // Check if a member with the same ID already exists in the team
//            boolean memberExists = team.getMembers().stream()
//                    .anyMatch(existingMember -> existingMember.getId() == member.getId());
//
//            if (memberExists) {
//                // Update the existing member
//                team.getMembers().forEach(existingMember -> {
//                    if (existingMember.getId() == member.getId()) {
//                        existingMember.setName(member.getName());
//                    }
//                });
//            } else {
//                // Add the member to the team
//                team.getMembers().add(member);
//            }
//
//            entityManager.merge(team); // Update the team in the database
//        }
//
//        return member;
//    }


    /**
     * Retrieves a user based on the provided email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The user with the specified email address, or {@code null} if no user is found.
     * @throws NoResultException If no user is found with the given email address.
     */
    @Override
    public User findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        try {
            return entityManager.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Handle the case where no user is found with the given email
            return null;
        }
    }


}
