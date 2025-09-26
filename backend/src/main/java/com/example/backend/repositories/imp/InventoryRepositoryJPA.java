package com.example.backend.repositories.imp;

import com.example.backend.models.*;
import com.example.backend.repositories.*;
import com.example.backend.services.EmailSenderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;


@Transactional
@Repository
@Primary
public class InventoryRepositoryJPA implements InventoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final EmailSenderService senderService;


    public InventoryRepositoryJPA(WarehouseRepository warehouseRepository, ProductRepository productRepository, OrdersRepository ordersRepository, UserRepository userRepository, EmailSenderService senderService) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.senderService = senderService;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public List<Inventory> findAll() {
        TypedQuery<Inventory> query =
                this.entityManager.createQuery(
                        "select i from Inventory i", Inventory.class);
        return query.getResultList();
    }

    @Override
    public Inventory findById(int id) {
        return entityManager.find(Inventory.class, id);

    }

    @Override
    @Transactional
    public Inventory addItem(Inventory item) {
        if (item.getId() == 0) {
            entityManager.persist(item);
        } else {
            item = entityManager.merge(item);
        }
        return item;
    }

    @Override
    public void updateItem(Inventory item) {
        entityManager.merge(item);
    }

//    @Override
//    public Inventory save(Inventory inventory) {
//        if (inventory.getId() == 0) {
//            calculateStatus(inventory);
//            entityManager.persist(inventory);
//        } else {
//            calculateStatus(inventory);
//            inventory = entityManager.merge(inventory);
//        }
//        changeQuantity(inventory.getProductId(), inventory.getId());
//        return inventory;
//    }

    @Override
    public Inventory save(Inventory inventory) {
        if (inventory.getId() == 0) {
            calculateStatus(inventory);
            entityManager.persist(inventory);
        } else {
            Inventory existingItem = entityManager.find(Inventory.class, inventory.getId());

            existingItem.setQuantity(inventory.getQuantity());
            existingItem.setMax_quantity(inventory.getMax_quantity());
            existingItem.setLow_stock_limit(inventory.getLow_stock_limit());

            calculateStatus(existingItem);
            entityManager.merge(existingItem);
        }

        if (inventory.getProductId() != null) {
            changeQuantity(inventory.getProductId(), inventory.getId());
        }

        return inventory;
    }

//    @Override
//    public void calculateStatus(Inventory inventory) {
//        int maxQuantity = inventory.getMax_quantity();
//        int quantity = inventory.getQuantity();
//
//        if (inventory.isPending()) {
//            inventory.setStatus(Inventory.STATUS.PENDING);
//        } else if (quantity <= 0) {
//            inventory.setStatus(Inventory.STATUS.NO_STOCK);
//        } else if (quantity < (maxQuantity/10 * 5)) {
//            inventory.setStatus(Inventory.STATUS.LOW);
//        } else if (quantity < (maxQuantity)) {
//            inventory.setStatus(Inventory.STATUS.HIGH);
//        }
//        else {
//            inventory.setStatus(Inventory.STATUS.FULL);
//        }
//    }

    @Override
    public void calculateStatus(Inventory inventory) {
        int maxQuantity = inventory.getMax_quantity();
        int quantity = inventory.getQuantity();
        int lowStockLimit = inventory.getLow_stock_limit();

        if (inventory.isPending()) {
            inventory.setStatus(Inventory.STATUS.PENDING);
        } else if (quantity <= 0) {
            inventory.setStatus(Inventory.STATUS.NO_STOCK);
        } else if (quantity < lowStockLimit) {
            inventory.setStatus(Inventory.STATUS.LOW);
            userRepository.findAll().
                    stream().
                    filter(user -> Objects.equals(user.getEmail(), "dherlaar@outlook.com")).
                    forEach(user -> sendMail(user.getEmail(), inventory.getWarehouseId(), inventory.getProductId()));
       } else if (quantity < (maxQuantity)) {
            inventory.setStatus(Inventory.STATUS.HIGH);
        } else {
            inventory.setStatus(Inventory.STATUS.FULL);
        }
    }

    @Override
    public void deleteItem(int id) {
        Inventory item = entityManager.find(Inventory.class, id);
        List<Orders> orders = item.getOrders();
        for(Orders order : orders) {
            ordersRepository.deleteOrder(order.getId());
        }
        entityManager.remove(item);
    }

    @Override
    public Inventory assignWarehouse(Inventory inventory, int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse != null) {
            inventory.setWarehouseId(warehouse);
            warehouse.getInventory().add(inventory);
            entityManager.merge(warehouse);
            entityManager.merge(inventory);
        }

        return inventory;
    }

//    @Override
//    public List<Inventory> findAggregatedInventory() {
//        List<Inventory> aggregatedItems = new ArrayList<>();
//
//        List<Inventory> inventoryList = getInventory();
//
//        Map<String, Integer> aggregatedInventory = new HashMap<>();
//
//        for (Inventory item : inventoryList) {
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
//        for (Map.Entry<String, Integer> entry : aggregatedInventory.entrySet()) {
//            String itemName = entry.getKey();
//            int totalQuantity = entry.getValue();
//
//            // Generate a unique id for each aggregated item
//            int uniqueId = generateUniqueId(itemName);
//
//            Inventory aggregatedItem = new Inventory(null, itemName, uniqueId,
//                    totalQuantity, false, "Total stock across warehouses");
//
//            aggregatedItems.add(aggregatedItem);
//        }
//
//
//        return aggregatedItems;
//    }
//
//    private int generateUniqueId(String itemName) {
//        List<Inventory> allInventory = findAll();
//
//        // Find the item with the specified itemName
//        Optional<Inventory> matchingItem = allInventory.stream()
//                .filter(item -> itemName.equals(item.getName()))
//                .findFirst();
//
//        // If the item is found, return its ID, otherwise return 0
//        return matchingItem.map(Inventory::getId).orElse(0);
//    }

    private List<Inventory> getInventory() {
        return findAll();
    }


    @Override
    public List<Inventory> buildDummyInventory() {
        return null;
    }

    @Override
    public List<Inventory> findWarehouseInventory(int warehouseId) {
        List<Inventory> allInventory = findAll();

        List<Inventory> warehouseInventory = new ArrayList<>();
        for (Inventory inventoryItem : allInventory) {
            Integer itemWarehouseId = inventoryItem.getWarehouseId();
            if (itemWarehouseId != null && itemWarehouseId.equals(warehouseId)) {
                warehouseInventory.add(inventoryItem);
            }
        }

        return warehouseInventory;
    }

    @Override
    public void changeQuantity(int productid, int inventoryId) {
        Product product = productRepository.findById(productid);
        Inventory inventory = this.findById(inventoryId);
        if (product != null && inventory != null) {
            List<Inventory> inventories = product.getInventories();
            if (inventories != null) {
                int totalQuantity = inventories.stream().mapToInt(Inventory::getQuantity).sum();
                product.setQuantity(totalQuantity);
            }
            productRepository.save(product);
        }

    }

    public void sendMail(String email, Integer warehouseId, Integer productId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        Product product = productRepository.findById(productId);

        senderService.sendEmail(email, "Low stock notification", String.format("This is a notification from Solar Sedum regarding the stock levels of our products.\n\n" +
                "We would like to inform you that the stock for the product '%s' in the warehouse '%s' is running low.\n" +
                "Please take necessary actions to replenish the stock to ensure smooth operations.\n\n" +
                "Current Stock Information:\n" +
                "- Product Name: %s\n" +
                "- Warehouse Name: %s\n\n" +
                "For any inquiries or assistance, please contact our support team at [support@solarsedum.com].\n\n" +
                "Thank you for your attention.\n\n" +
                "Best regards,\n\n" +
                "Dannièl Herlaar\n" +
                "IT DEV\n" +
                "Solar Sedum\n" +
                "dannielherlaar@gmail.com\n", product.getName(), warehouse.getName(), product.getName(), warehouse.getName()));
    }

}
