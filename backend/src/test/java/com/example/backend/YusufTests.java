package com.example.backend;

import com.example.backend.controllers.ForecastController;
import com.example.backend.controllers.OrdersController;
import com.example.backend.models.Forecast;
import com.example.backend.models.Orders;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.ForecastRepository;
import com.example.backend.repositories.OrdersRepository;
import com.example.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class YusufTests {

    @Mock
    private ForecastRepository forecastRepository;

    @InjectMocks
    private ForecastController forecastController;


    @Test
    void getForecastById_NotNull() {
        // Arrange
        Mockito.when(forecastRepository.findById(anyInt())).thenReturn(Mockito.mock(Forecast.class));

        // Act
        Forecast result = forecastController.getForecastById(1);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllForecasts_NotNull() {
        // Arrange
        List<Forecast> mockOrdersList = new ArrayList<>();
        Forecast forecast1 = new Forecast();
        Forecast forecast2 = new Forecast();
        forecast1.setOrderQuantity(1);
        forecast2.setOrderQuantity(1);
        mockOrdersList.add(forecast1);
        mockOrdersList.add(forecast2);

        Mockito.when(forecastRepository.findAll()).thenReturn(mockOrdersList);

        // Act
        List<Forecast> result = forecastController.getAllForecast();

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllForecast_ThrowsNotFoundException() {
        // Arrange
        Mockito.when(forecastRepository.findAll()).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, forecastController::getAllForecast);
    }


    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersController ordersController;

    @Test
    void getAllOrders_NotNull() {
        // Arrange
        List<Orders> mockOrdersList = new ArrayList<>();
        Orders orders1 = new Orders();
        Orders orders2 = new Orders();
        orders1.setQuantity(1);
        orders2.setQuantity(1);
        mockOrdersList.add(orders1);
        mockOrdersList.add(orders2);

        Mockito.when(ordersRepository.findAll()).thenReturn(mockOrdersList);

        // Act
        List<Orders> result = ordersController.getAllOrders();

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllOrders_ThrowsNotFoundException() {
        // Arrange
        Mockito.when(ordersRepository.findAll()).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, ordersController::getAllOrders);
    }
}
