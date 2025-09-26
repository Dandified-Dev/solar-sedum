//package com.example.backend.repositories.imp;
//
//import com.example.backend.models.Inventory;
//import com.example.backend.models.Warehouse;
//import com.example.backend.repositories.InventoryRepository;
//import com.example.backend.repositories.WarehouseRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//
//@Repository
//public class InventoryRepositoryMock implements InventoryRepository {
//
//    List<Inventory> inventory;
//    WarehouseRepository warehouseRepositoryMock;
//
//    public InventoryRepositoryMock(WarehouseRepository warehouseRepositoryMock) {
//        this.warehouseRepositoryMock = warehouseRepositoryMock;
//        init();
//    }
//
//
//private void init() {
//    this.inventory = buildDummyInventory();
//}
//
//
//    @Override
//    public List<Inventory> findAll() {
//        return getInventory();
//    }
//
//    @Override
//    public Inventory findById(int id) {
//        for (Inventory inventory1: getInventory()) {
//            if(inventory1.getId() == id){
//                return inventory1;
//            }
//        }
//        return null;
//    }
//
//    public List<Inventory> findWarehouseInventory(int id){
//        ArrayList<Inventory> returnList = new ArrayList<>();
//        for (Inventory inventory1: inventory) {
//            if (inventory1.getWarehouseId() == id) {
//                returnList.add(inventory1);
//            }
//        }
//        return returnList;
//    }
//
//    @Override
//    public void addItem(Inventory item) {
//    }
//
//    @Override
//    public void updateItem(Inventory item) {
//    }
//
//    @Override
//    public void deleteItem(int id) {
//    }
//
//
//    @Override
//    public Inventory assignWarehouse(Inventory inventory, int warehouseId){
//        Warehouse warehouse = warehouseRepositoryMock.findById(warehouseId);
//       if(warehouse != null){
//           inventory.setWarehouseId(warehouse);
//           List<Inventory> inventories = warehouse.getInventory();
//           inventories.add(inventory);
//           warehouse.setInventory(inventories);
//       }
//       return inventory;
//    }
//
//    public List<Inventory> findAggregatedInventory() {
//        Map<String, Integer> aggregatedInventory = new HashMap<>();
//
//        for (Inventory item : getInventory()) {
//            String itemName = item.getName();
//            int itemQuantity = item.getQuantity();
//
//            if (aggregatedInventory.containsKey(itemName)) {
//                int currentQuantity = aggregatedInventory.get(itemName);
//                aggregatedInventory.put(itemName, currentQuantity + itemQuantity);
//            } else {
//                aggregatedInventory.put(itemName, itemQuantity);
//            }
//        }
//
//        List<Inventory> aggregatedItems = new ArrayList<>();
//        for (Map.Entry<String, Integer> entry : aggregatedInventory.entrySet()) {
//            String itemName = entry.getKey();
//            int totalQuantity = entry.getValue();
//
//            Inventory aggregatedItem = new Inventory("image_path", itemName, 0,
//                    totalQuantity, false, "Total stock across warehouses");
//            aggregatedItems.add(aggregatedItem);
//        }
//        return aggregatedItems;
//    }
//
//
//public List<Inventory> buildDummyInventory() {
//    inventory = new ArrayList<>();
//
//    inventory.add(new Inventory("@/assets/inventory/solar_panels.png",
//            "solar panels", 1, (int) Math.round(Math.random() * 30),
//            false, ""));
//    inventory.add(new Inventory("@/assets/inventory/grass.png",
//            "grass", 2, (int) Math.round(Math.random() * 30),
//            true, "amount is in m³"));
//    inventory.add(new Inventory("@/assets/inventory/steel_foundation.png",
//            "steel foundation", 3, (int) Math.round(Math.random() * 30),
//            false, ""));
//    inventory.add(new Inventory("@/assets/inventory/plants.png",
//            "plants", 4, (int) Math.round(Math.random() * 30),
//            false, "vergeet ze geen water te geven ;)"));
//    inventory.add(new Inventory("@/assets/inventory/hammer.png",
//            "hammer", 5, (int) Math.round(Math.random() * 30),
//            false, ""));
//    List<Inventory> items = new ArrayList<>();
//
//    for (Warehouse warehouse : warehouseRepositoryMock.findAll()) {
//        List<Integer> usedItemIndexes = new ArrayList<>();
//
//        List<Inventory> warehouseItems = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            int randomIndex;
//            do {
//                randomIndex = (int) (Math.random() * inventory.size());
//            } while (usedItemIndexes.contains(randomIndex));
//            usedItemIndexes.add(randomIndex);
//
//            Inventory randomItem = inventory.get(randomIndex);
//            int randomQuantity = (int) (Math.random() * 50) + 1;
//            Integer warehouseId = warehouse.getId();
//
//            Inventory item = new Inventory(
//                    randomItem.getPicture(),
//                    randomItem.getName(),
//                    randomItem.getId(),
//                    randomQuantity,
//                    randomItem.isPending(),
//                    randomItem.getDescription(),
//                    warehouse
//            );
//            assignWarehouse(item, warehouseId);
//            warehouseItems.add(item);
//        }
//        items.addAll(warehouseItems);
//        warehouse.setInventory(warehouseItems);
//    }
//    return items;
//}
//
//
//
//    public List<Inventory> getInventory() {
//        return inventory;
//    }
//}
