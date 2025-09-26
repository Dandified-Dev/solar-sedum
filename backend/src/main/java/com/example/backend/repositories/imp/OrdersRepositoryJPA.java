package com.example.backend.repositories.imp;

import com.example.backend.models.Inventory;
import com.example.backend.models.Orders;
import com.example.backend.repositories.InventoryRepository;
import com.example.backend.repositories.OrdersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of the repository for handling Orders entities.
 */
@Transactional
@Repository
@Primary
public class OrdersRepositoryJPA implements OrdersRepository {

    private InventoryRepository inventoryRepository;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Get a list of all orders.
     *
     * @return List of orders.
     */
    @Override
    public List<Orders> findAll() {
        TypedQuery<Orders> query =
                this.entityManager.createQuery(
                        "select o from Orders o", Orders.class);
        return query.getResultList();
    }

    /**
     * Find an order by its ID.
     *
     * @param id The ID of the order.
     * @return The found order or null if not found.
     */
    @Override
    public Orders findById(int id) {
        return entityManager.find(Orders.class, id);
    }

    /**
     * Find orders associated with a specific inventory by inventory ID.
     *
     * @param id The ID of the inventory.
     * @return List of orders associated with the inventory.
     */
    @Override
    public List<Orders> findByInventoryId(int id) {
        List<Orders> allOrders = findAll();
        return allOrders.stream().filter(
                        order -> order.getInventory() != null && order.getInventory().getId() == id)
                .collect(Collectors.toList());
    }

    /**
     * Calculate the quantity left of a specific inventory by subtracting the total ordered quantity.
     *
     * @param id The ID of the inventory.
     * @return The quantity left in the inventory.
     */
    @Override
    public int calculateQuantityLeftOfInventory(int id) {
        int inventoryQuantity = inventoryRepository.findById(id).getQuantity();
        List<Orders> orders = findByInventoryId(id);
        int totalOrderedQuantity = orders.stream()
                .mapToInt(Orders::getQuantity)
                .sum();
        return inventoryQuantity - totalOrderedQuantity;
    }

    /**
     * Save an order to the database.
     *
     * @param orders The order to save.
     * @return The saved order.
     */
    @Override
    public Orders save(Orders orders) {
        if (orders.getId() == 0) {
            // If the order ID is not set, persist it as a new order
            entityManager.persist(orders);
        } else {
            // If the order ID is set, merge the order with the existing one
            orders = entityManager.merge(orders);
        }
        return orders;
    }

    /**
     * Delete an order by its ID.
     *
     * @param id The ID of the order to delete.
     */
    @Override
    public void deleteOrder(int id) {
        // Remove the order from the database by its ID
//        Inventory inventory = inventoryRepository.findById(findById(id).getInventory().getId());
//        inventory.setQuantity(inventory.getQuantity() + findById(id).getQuantity());
        entityManager.remove(findById(id));
//        inventoryRepository.save(inventory);
    }
}
