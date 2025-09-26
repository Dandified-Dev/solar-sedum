package com.example.backend;

import com.example.backend.controllers.WarehousesController;
import com.example.backend.models.Team;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class DavidTests {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehousesController warehousesController;

    @Test
    void testFindWarehouseById_NotNull() {
        // Fast: under 200ms
        // Arrange
        Mockito.when(warehouseRepository.findById(anyLong())).thenReturn(Mockito.mock(Warehouse.class));

        // Act
        Warehouse result = warehousesController.getWarehouse(1952);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testDeleteWarehouseById_Null() {
        // Repeatable: the data isn't actually gone and can be used again
        // Arrange
        Mockito.when(warehouseRepository.findById(anyLong())).thenReturn(Mockito.mock(Warehouse.class));

        // Act
        warehousesController.deleteWarehouse(3602);

        // Assert
        verify(warehouseRepository, times(1)).findById(3602);
        verify(warehouseRepository, times(1)).deleteById(3602);
    }


    @Test
    void testFindTeamById_NotNull() {
        // Arrange
        Mockito.when(warehouseRepository.findTeam(anyInt(), anyInt())).thenReturn(Mockito.mock(Team.class));

        // Act
        Team result = warehouseRepository.findTeam(3552, 42);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testRemoveTeam_NoTeam() {
        // Arrange
        // Timely: Mock data makes it so that there is no extra time with an increase of the db data
        Mockito.when(warehouseRepository.findById(anyLong())).thenReturn(Mockito.mock(Warehouse.class));
        Mockito.when(warehouseRepository.findTeam(anyInt(), anyInt())).thenReturn(Mockito.mock(Team.class));


        // Act
        warehousesController.removeTeam(3602, 42);

        // Assert
        verify(warehouseRepository, times(1)).findById(3602);
        verify(warehouseRepository, times(1)).removeTeam(anyInt(), any(Team.class));
    }

    @Test
    void testUpdateWarehouse_Updated() {
        // Arrange
        // Isolated: No unnecessary extra code
        int warehouseId = 3602;

        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setId(warehouseId);

        Warehouse updatedWarehouse = new Warehouse();
        updatedWarehouse.setId(warehouseId);
        updatedWarehouse.setLocation("Test");

        when(warehouseRepository.findById(warehouseId)).thenReturn(existingWarehouse);
        when(warehouseRepository.update(updatedWarehouse)).thenReturn(updatedWarehouse);

        // Act
        Warehouse result = warehousesController.updateWarehouse(updatedWarehouse, warehouseId);

        // Assert
        assertNotNull(result);
        assertEquals(updatedWarehouse.getLocation(), result.getLocation());

        verify(warehouseRepository, times(1)).findById(warehouseId);
        verify(warehouseRepository, times(1)).update(updatedWarehouse);
    }

    @Test
    void testFindAll_NotNull() {
        // Arrange
        // Self verifying: shows the results and will also give an error where it goes wrong
        List<Warehouse> mockWarehouseList = new ArrayList<>();
        Warehouse warehouse1 = new Warehouse();
        Warehouse warehouse2 = new Warehouse();
        warehouse1.setLocation("Arnhem");
        warehouse2.setLocation("Utrecht");
        mockWarehouseList.add(warehouse1);
        mockWarehouseList.add(warehouse2);

        Mockito.when(warehouseRepository.findAll()).thenReturn(mockWarehouseList);

        // Act
        List<Warehouse> result = warehousesController.getWarehouses();

        // Assert
        assertNotNull(result);
    }
}

