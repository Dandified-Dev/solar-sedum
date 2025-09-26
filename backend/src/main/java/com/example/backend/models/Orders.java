package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    public enum STATUS {
        ARRIVED, REQUESTED
    }

    @OneToMany(mappedBy = "orders", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Forecast> forecast;



    public Orders() {
    }

    public Orders(int id, int quantity, STATUS status, Project project, Inventory inventory, List<Forecast> forecast){
        this.id = id;
        this.quantity = quantity;
        this.status = status;
        this.project = project;
        this.inventory = inventory;
        this.forecast = forecast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        if (project != null){
            return project;
        }
        return null;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Inventory getInventory() {
        if (inventory != null) {
            return inventory;
        }
        return null;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
