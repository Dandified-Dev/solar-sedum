package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for handling HTTP requests related to forecast.
 */
@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private ForecastRepository forecastRepository;

    @Autowired
    public ForecastController(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    /**
     * Get a list of all forecasts.
     *
     * @return List of forecasts.
     * @throws NotFoundException If no forecasts are found.
     */
    @GetMapping
    public List<Forecast> getAllForecast() {
        // Get all forecasts from the repository
        List<Forecast> forecast = forecastRepository.findAll();
        // Check if forecasts exist
        if (forecast == null) {
            // Throw an exception if no forecasts are found
            throw new NotFoundException();
        }
        return forecast;
    }

    /**
     * Get a specific forecast by ID.
     *
     * @param forecastId The ID of the forecast to retrieve.
     * @return The forecast with the given ID.
     */
    @GetMapping("/{forecastId}")
    public Forecast getForecastById(@PathVariable int forecastId) {
        // Find the forecast by ID
        Forecast forecast = forecastRepository.findById(forecastId);
        if (forecast == null) {
            // Throw an exception if the forecast with the given ID is not found
            throw new ResourceNotFoundException("Forecast " + forecastId + " not found");
        }
        return forecast;
    }

    /**
     * Get a list of forecasts associated with a specific order.
     *
     * @param id The ID of the order.
     * @return List of forecasts associated with the order.
     */
    @GetMapping("/orders/{id}")
    public List<Forecast> getForecastByOrderId(@PathVariable int id) {
        // Filter forecasts by order ID
        return forecastRepository.findAll().stream()
                .filter(forecast -> forecast.getOrders().getId() == id)
                .collect(Collectors.toList());
    }

    /**
     * Add forecasts to the system.
     *
     * @param forecasts List of forecasts to add.
     * @return The added forecasts.
     */
    @PostMapping("")
    public Forecast addForecast(@RequestBody List<Forecast> forecasts) {
        for (Forecast forecast : forecasts) {
            // Save each forecast to the repository
            return forecastRepository.save(forecast);
        }
        return null;
    }
}