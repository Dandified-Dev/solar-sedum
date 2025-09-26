package com.example.backend.repositories;

import com.example.backend.models.Project;
import com.example.backend.models.Team;

import java.util.List;

public interface ProjectRepository {
    List<Project> findAll();
    Project findById(long id);
    Project save(Project project);
    void deleteById(long id);
    Project assignTeam(Project project, int teamId);
    Project buildDummyProject();
}
