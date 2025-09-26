package com.example.backend.repositories;

import com.example.backend.models.Inventory;
import com.example.backend.models.Orders;

import java.util.List;

public interface OrdersRepository {
    List<Orders> findAll();

    Orders findById(int id);
    List<Orders> findByInventoryId(int id);
    int calculateQuantityLeftOfInventory(int id);

    Orders save(Orders orders);

    void deleteOrder(int id);
}
