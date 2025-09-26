package com.example.backend.repositories;
import com.example.backend.models.Forecast;
import com.example.backend.models.Orders;

import java.util.List;

public interface ForecastRepository {
    List<Forecast> findAll();
    Forecast findById(int id);
    Forecast save(Forecast forecast);

}
