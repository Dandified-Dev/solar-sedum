package com.example.backend;

import com.example.backend.models.Inventory;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.imp.InventoryRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class InventoryRepositoryTest {
    @Autowired
    private InventoryRepositoryJPA inventoryRepositoryJPA;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void saveInventoryTest(){
        // Arrange
        Inventory testItem = inventoryRepositoryJPA.findById(433);

        // Action
        Inventory savedItem = inventoryRepositoryJPA.save(testItem);

        // Assert
        assertNotNull(savedItem);
    }

    @Test
    public void deleteInventoryItemTest(){
        // Arrange
        Inventory testItem = inventoryRepositoryJPA.findById(351);
        Inventory savedItem = inventoryRepositoryJPA.save(testItem);

        // Action
        inventoryRepositoryJPA.deleteItem(savedItem.getId());

        // Assert
        Inventory deletedItem = inventoryRepositoryJPA.findById(savedItem.getId());
        assertNull(deletedItem);
    }
}
