package com.example.backend.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name= "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouseId;

    @OneToMany(mappedBy = "teamId", cascade = CascadeType.PERSIST)
    private List<User> users;

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST)
    private List<Project> projects;

    public Team(String name, Warehouse warehouseId, List<User> users, List<Project> projects){
        this.name = name;
        this.users = users;
        this.warehouseId = warehouseId;
        this.projects = projects;
    }

    public Team(int id){
        this.id = id;
    }
    public Team(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWarehouseId() {
        if (warehouseId != null) {
            return warehouseId.getId();
        }
        return null;
    }


    public void setWarehouseId(Warehouse warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<User> getMembers() {
        return users;
    }

    public void setMembers(List<User> users) {
        this.users = users;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
