package com.example.backend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Forecast")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
    private int orderQuantity;
    private int stock;
    private int quantityAfter;
    private Date date;

    public Forecast() {
    }

    public Forecast(int id, int orderQuantity, int stock, int quantityAfter, Date date, Orders orders) {
        this.id = id;
        this.orderQuantity = orderQuantity;
        this.stock = stock;
        this.quantityAfter = quantityAfter;
        this.date = date;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public int getQuantityAfter() {
        return quantityAfter;
    }

    public void setQuantityAfter(int quantityAfter) {
        this.quantityAfter = quantityAfter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
