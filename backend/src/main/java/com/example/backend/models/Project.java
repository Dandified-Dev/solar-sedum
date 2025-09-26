package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;

import java.util.Date;
import java.util.List;

@Entity
@Table(name= "Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String location;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private Date startDate;
    private Date dueAt;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name = "Orders",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "inventory_id")
//    )
    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Orders> orders;

    public Project() {
    }

    public enum Status{
        UNSTARTED, ONGOING, DONE
    }
    public Project(int id, String name, String location, String description, Status status, Team team, Date startDate, Date dueAt, List<Orders> orders){
        this.id = id;
        this.name=name;
        this.location=location;
        this.description=description;
        this.status=status;
        this.team=team;
        this.startDate = startDate;
        this.dueAt = dueAt;
        this.orders = orders;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTeamId() {
        if (team != null){
            return team.getId();
        }
        return null;
    }

    public void setTeamId(Team team) {
        this.team = team;
    }

    public Integer getWarehouse() {
        if (warehouse != null){
            return warehouse.getId();
        }
        return null;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public List<Orders> getOrders() {
        if (orders != null) {
            return orders;
        }
        return null;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
