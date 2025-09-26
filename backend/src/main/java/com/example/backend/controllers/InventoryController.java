package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.PreConditionFailedException;
import com.example.backend.models.*;
import com.example.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    private final TeamRepository teamRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public InventoryController(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository,
            WarehouseRepository warehouseRepository,
            TeamRepository teamRepository,
            OrdersRepository ordersRepository) {     
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.teamRepository = teamRepository;
        this.ordersRepository = ordersRepository;
    }

    @GetMapping
    public List<Inventory> getAllItems() {
        List<Inventory> inventory = inventoryRepository.findAll();
        if (inventory == null) {
            throw new NotFoundException();
        }
        return inventory;
    }


    @GetMapping("/getWarehouse/{warehouseId}")
    public List<Inventory> findWarehouseInventory(@PathVariable int warehouseId) {
        List<Inventory> inventory = inventoryRepository.findWarehouseInventory(warehouseId);
        if (inventory.isEmpty()) {
            throw new NotFoundException();
        }
        return inventory;
    }

    @GetMapping("/{id}")
    public Inventory getItemById(@PathVariable int id) {
        Inventory inventory = inventoryRepository.findById(id);
        if (inventory == null) {
            throw new NotFoundException();
        }
        return inventoryRepository.findById(id);
    }

    @GetMapping("teams/{teamId}")
    public List<Inventory> getItemByTeamId(@PathVariable int teamId) {
        Team team = teamRepository.findById(teamId);

        if (team == null) {
            throw new NotFoundException();
        }

        Warehouse warehouse = warehouseRepository.findById(team.getWarehouseId());

        if (warehouse == null) {
            throw new NotFoundException();
        }

        return inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getWarehouseId().equals(warehouse.getId()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/warehouse/{warehouseId}")
    public Inventory assignToWarehouse(@PathVariable int itemId, @PathVariable int warehouseId) {
        Inventory foundItem = inventoryRepository.findById(itemId);
        if (foundItem == null) {
            throw new NotFoundException();
        }

        return inventoryRepository.assignWarehouse(foundItem, warehouseId);
    }


//    @GetMapping("/aggregatedInventory")
//    public List<Inventory> getAggregatedInventory() {
//        List<Inventory> aggregatedInventory = inventoryRepository.findAggregatedInventory();
//        if (aggregatedInventory.isEmpty()) {
//            throw new NotFoundException();
//        }
//        return aggregatedInventory;
//    }

    @PostMapping("/{productId}/{warehouseId}")
    public Inventory addItemWithWarehouse(@RequestBody Inventory item, @PathVariable(required = false) Integer productId,
                                        @PathVariable(required = false) Integer warehouseId) {

        if (productId != null){
            Product foundProduct = productRepository.findById(productId);
            item.setProductId(foundProduct);

        }

        if (warehouseId != null) {
            Warehouse foundWarehouse = warehouseRepository.findById(warehouseId);
            item.setWarehouseId(foundWarehouse);
        }

        return inventoryRepository.save(item);
    }


    @PostMapping("")
    public Inventory addItem(@RequestBody Inventory item) {
        Inventory existingItem = inventoryRepository.findById(item.getId());
        if (existingItem != null) {
            Product foundProduct = productRepository.findById(existingItem.getProductId());
            Warehouse foundWarehouse = warehouseRepository.findById(existingItem.getWarehouseId());
            item.setWarehouseId(foundWarehouse);
            item.setProductId(foundProduct);
        }
        return inventoryRepository.save(item);
    }

    @PutMapping(path = "/updateInventory/{id}", produces = "application/json")
    public Inventory updateInventory(
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) Integer lowStockLimit,
            @PathVariable int id
    ) {
        Inventory existingItem = inventoryRepository.findById(id);

        if (existingItem != null) {
            if (quantity != null) {
                existingItem.setQuantity(quantity);
            }

            if (maxQuantity != null) {
                existingItem.setMax_quantity(maxQuantity);
            }

            if (lowStockLimit != null) {
                existingItem.setLow_stock_limit(lowStockLimit);
            }

        }
        return inventoryRepository.save(existingItem);

    }



    @PutMapping(path = "{id}", produces = "application/json")
    public void updateItem(@RequestBody Inventory item, @PathVariable int id) {
//        Inventory existingItem = inventoryRepository.findById(id);
//        System.out.println(existingItem.getProductId());
//        if (existingItem.getProductId() != null){
//            System.out.println("TESTTTTTTTTTTTT!!!!!!!!!!!!!11-----------------------------------------------");
//            Product foundProduct = productRepository.findById(existingItem.getProductId());
//            item.setProductId(foundProduct);
//        }
//
//        if (item.getId() != id) {
//            throw new PreConditionFailedException("Product ID in path does not match ID in request body");
//        }

        Inventory existingItem = inventoryRepository.findById(id);
        if (existingItem != null) {
            Product foundProduct = productRepository.findById(existingItem.getProductId());
            item.setProductId(foundProduct);
        }
        if (existingItem != null) {
            Warehouse foundWarehouse = warehouseRepository.findById(existingItem.getWarehouseId());
            item.setWarehouseId(foundWarehouse);
        }

        inventoryRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable int id) {
        List<Orders> existingOrder = ordersRepository.findByInventoryId(id);


        inventoryRepository.deleteItem(id);

    }
}

