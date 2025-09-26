package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.models.Forecast;
import com.example.backend.models.Inventory;
import com.example.backend.models.Project;
import com.example.backend.repositories.ForecastRepository;
import com.example.backend.repositories.InventoryRepository;
import com.example.backend.repositories.OrdersRepository;
import com.example.backend.repositories.ProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.models.Orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for handling HTTP requests related to orders.
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private OrdersRepository ordersRepository;
    private ProjectRepository projectRepository;
    private InventoryRepository inventoryRepository;
    private ForecastRepository forecastRepository;

    @Autowired
    public OrdersController(OrdersRepository ordersRepository, ProjectRepository projectRepository, InventoryRepository inventoryRepository, ForecastRepository forecastRepository) {
        this.ordersRepository = ordersRepository;
        this.projectRepository = projectRepository;
        this.inventoryRepository = inventoryRepository;
        this.forecastRepository = forecastRepository;
    }

    /**
     * Get a list of all orders.
     *
     * @return List of orders.
     * @throws NotFoundException If no orders are found.
     */
    @GetMapping
    public List<Orders> getAllOrders() {
        // Get all orders
        List<Orders> orders = ordersRepository.findAll();
        // Check if orders exist
        if (orders == null) {
            // Throw an exception if no orders are found
            throw new NotFoundException();
        }
        return orders;
    }

    /**
     * Get a list of orders associated with a specific project.
     *
     * @param id The ID of the project.
     * @return List of orders associated with the project.
     */
    @GetMapping("/project/{id}")
    public List<Orders> getOrdersByProjectId(@PathVariable int id) {
        // Filter orders by project ID
        return ordersRepository.findAll().stream().filter(
                orders1 -> orders1.getProject().getId() == id
        ).collect(Collectors.toList());
    }

    /**
     * Get the total quantity left of a specific inventory.
     *
     * @param inventoryId The ID of the inventory.
     * @return The total quantity left of the inventory.
     */
    @GetMapping("/{inventoryId}")
    public int totalQuantity(@PathVariable int inventoryId) {
        // Calculate total quantity left of the inventory
        return ordersRepository.calculateQuantityLeftOfInventory(inventoryId);
    }

    @GetMapping("/inventory/{inventoryId}")
    public List<Orders> findByInventoryId(@PathVariable int inventoryId) {
        return ordersRepository.findByInventoryId(inventoryId);
    }

    /**
     * Add orders to the system.
     *
     * @return The added orders.
     */
    @PostMapping("")
    public List<Orders> addOrders(@RequestBody List<Orders> ordersList) {
        List<Orders> savedOrders = new ArrayList<>();

        for (Orders order : ordersList) {
            // Check if orders already exist
            Orders existingOrders = ordersRepository.findById(order.getId());
            if (existingOrders != null) {
                // If orders exist, associate them with the corresponding inventory and project
                Inventory foundInventory = inventoryRepository.findById(existingOrders.getInventory().getId());
                Project foundProject = projectRepository.findById(existingOrders.getProject().getId());
                order.setInventory(foundInventory);
                order.setProject(foundProject);
            }

            // Save the order to the repository
            Orders savedOrder = ordersRepository.save(order);
            savedOrders.add(savedOrder);


            //This is for forecasting made by yusuf to make forecasting work
            if (order.getForecast() != null && !order.getForecast().isEmpty()) {
                for (Forecast forecast : order.getForecast()) {
                    // Set the order for the forecast
                    forecast.setOrders(savedOrder);

                    // Save the forecast
                    forecastRepository.save(forecast);
                }
            }
        }
        return savedOrders;
    }




    /**
     * Add orders with associated project and inventory.
     *
     * @param orders      The orders to add.
     * @param projectId   The ID of the project (optional).
     * @param inventoryId The ID of the inventory (optional).
     * @return The added orders.
     */
    @PostMapping("/{projectId}/{inventoryId}")
    public Orders addOrdersWithInventoryAndProject(@RequestBody Orders orders, @PathVariable(required = false) Integer projectId, @PathVariable(required = false) Integer inventoryId) {
        if (projectId != null) {
            // If project ID is provided, associate the orders with the project
            Project foundProject = projectRepository.findById(projectId);
            System.out.println(foundProject);
            orders.setProject(foundProject);
        }
        if (inventoryId != null) {
            // If inventory ID is provided, associate the orders with the inventory and update quantity
            Inventory foundInventory = inventoryRepository.findById(inventoryId);
            System.out.println(foundInventory);
            orders.setInventory(foundInventory);
            foundInventory.setQuantity(foundInventory.getQuantity() - orders.getQuantity());
            inventoryRepository.save(foundInventory);
        }

        orders.setStatus(Orders.STATUS.REQUESTED);

        return ordersRepository.save(orders);
    }


    /**
     * Update orders based on the provided JSON data.
     *
     * @param data The JSON data containing orders information.
     * @return The updated orders.
     */
    @PutMapping("/update-orders")
    public List<Orders> updateOrders(@RequestBody JsonNode data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert JSON data to Orders object
        List<Orders> orders = objectMapper.readValue(data.get("orders").toString(), new TypeReference<>() {});
        int projectId = data.get("projectId").asInt();
        List<Integer> inventoryIds = objectMapper.readValue(data.get("inventoryIds").traverse(), new TypeReference<>() {});
        List<Orders> updatedOrders = new ArrayList<>();
        List<Orders> initialOrders = ordersRepository.findAll().stream().filter(orders1 -> orders1.getProject().getId() == projectId).toList();
        System.out.println(" teeeeeeeeeeeeeeeeeeeeeeeessrstsrttfghfgghjkjkjkjkjkjkkkkkkkkkkkkkkkk");
        System.out.println(inventoryIds);

        // Validate input parameters
        if (projectId <= 0 || inventoryIds.isEmpty()) {
            System.out.println("Invalid parameters");
            // Handle invalid parameters
            return null;
        }

        // Iterate through the initial orders list
        for (Orders initialOrder : initialOrders) {
            // If the order is not present in the current orders list, delete it
            if (orders.stream().noneMatch(order -> order.getId() == initialOrder.getId())) {
                // Delete or handle the order as needed
                ordersRepository.deleteOrder(initialOrder.getId());
            }
        }
        System.out.println();
        // Iterate through the current orders list and corresponding inventory IDs
        for (int i = 0; i < orders.size(); i++) {
            Orders order = orders.get(i);
            Integer orderInventoryId = inventoryIds.get(i);
            System.out.println(order);
            System.out.println(orderInventoryId);

            // Check if the existing order, project, and inventory exist
            Orders existingOrder = ordersRepository.findById(order.getId());
            Project foundProject = projectRepository.findById(projectId);
            Inventory foundInventory = inventoryRepository.findById(orderInventoryId);

            // Update project and inventory
            updateProjectAndInventory(order, foundProject, foundInventory, existingOrder);

            // Set order status
            order.setStatus(Orders.STATUS.REQUESTED);
            System.out.println(updatedOrders.size());
            // Add the updated order to the list
            updatedOrders.add(order);
        }

        System.out.println(orders.size());
        updatedOrders.forEach(order -> ordersRepository.save(order));
        return updatedOrders;
    }

    /**
     * Helper method to update project and inventory.
     *
     * @param orders         The updated orders object.
     * @param project        The project to associate with the orders.
     * @param inventory      The inventory to associate with the orders.
     * @param existingOrder  The existing orders object to calculate quantity changes.
     */
    private void updateProjectAndInventory(Orders orders, Project project, Inventory inventory, Orders existingOrder) {
        // Update project and inventory logic
        if (existingOrder != null){
            inventory.setQuantity(inventory.getQuantity() + existingOrder.getQuantity() - orders.getQuantity());
            inventoryRepository.save(inventory);
        }
        else{
            inventory.setQuantity(inventory.getQuantity() - orders.getQuantity());
            inventoryRepository.save(inventory);
        }
        orders.setProject(project);
        orders.setInventory(inventory);
    }

    /**
     * Delete an order by its ID.
     *
     * @param id The ID of the order to delete.
     */
    @DeleteMapping(path = "{id}", produces = "application/json")
    public void deleteOrder(@PathVariable int id) {
        // Delete the order by ID
        ordersRepository.deleteOrder(id);
    }
}
