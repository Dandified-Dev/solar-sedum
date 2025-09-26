package com.example.backend.models;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name= "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int quantity;
    private Double price;
    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL)
    private List<Inventory> inventories;

    public Product(){}

    public Product(int id, String name, String description, List<Inventory> inventories, int quantity, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inventories = inventories;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public List<Inventory> getInventories() {
        if (inventories != null) {
            return inventories;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
