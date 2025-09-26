package com.example.backend.repositories;
import com.example.backend.models.Inventory;

import java.util.List;

public interface InventoryRepository {

    List<Inventory> findAll();

    Inventory findById(int id);

    Inventory addItem(Inventory item);

    void updateItem(Inventory item);

    Inventory save(Inventory inventory);

    void deleteItem(int id);
    Inventory assignWarehouse(Inventory inventory, int warehouseId);

//    List<Inventory> findAggregatedInventory();

    List<Inventory> buildDummyInventory();

    List<Inventory> findWarehouseInventory(int warehouseId);

    void changeQuantity(int productId, int inventoryId);

    void calculateStatus(Inventory inventory);
}
