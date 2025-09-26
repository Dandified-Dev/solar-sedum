package com.example.backend;

import com.example.backend.controllers.InventoryController;
import com.example.backend.models.Inventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryControllerTest {
    @Autowired
    private InventoryController inventoryController;

    @Test
    public void addItemWithWarehouseTest(){
        // Arrange
        Inventory testItem = inventoryController.getItemById(445);

        // Action
        Inventory result = inventoryController.addItemWithWarehouse(testItem, 1, 1);


        // Assert
        assertNotNull(result, "result should not be null");

    }

    @Test
    public void getAllItemsTest(){
        // Arrange

        // Action
        List<Inventory> allItems = inventoryController.getAllItems();

        // Assert
        assertNotNull(allItems);
        assertEquals(21, allItems.size(), "should be 21 or check database????");

    }

    @Test
    public void updateInventoryTest(){
        // Arrange
        Inventory testItem = inventoryController.getItemById(442);
        Inventory result = inventoryController.addItemWithWarehouse(testItem, 1, 1);

        // Action
        Inventory updatedItem = inventoryController.updateInventory(60, 250, 30, result.getId());

        // Assert
        assertNotNull(updatedItem, "updatedItem should not be null");
        assertEquals(60, updatedItem.getQuantity(), "quantity should be 60");
        assertEquals(250, updatedItem.getMax_quantity(), "max_quantity should be 250");
        assertEquals(30, updatedItem.getLow_stock_limit(), "low_stock_limit should be 30");
    }

}
