package com.example.backend.repositories.imp;

import com.example.backend.models.Inventory;
import com.example.backend.models.Forecast;
import com.example.backend.models.Orders;
import com.example.backend.repositories.ForecastRepository;
import com.example.backend.repositories.InventoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of the repository for handling Orders entities.
 */
@Transactional
@Repository
@Primary
public class ForecastRepositoryJPA implements ForecastRepository {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Get a list of all forecast.
     *
     * @return List of forecast.
     */
    @Override
    public List<Forecast> findAll() {
        TypedQuery<Forecast> query =
                this.entityManager.createQuery(
                        "select o from Forecast o", Forecast.class);
        return query.getResultList();
    }

    /**
     * Find an forecast by its ID.
     *
     * @param id The ID of the forecast.
     * @return The found forecast or null if not found.
     */
    @Override
    public Forecast findById(int id) {
        return entityManager.find(Forecast.class, id);
    }

    /**
     * Save a forecast to the database.
     *
     * @param forecast The forecast to save.
     * @return The saved forecast.
     */
    @Override
    public Forecast save(Forecast forecast) {
        if (forecast.getId() == 0) {

            entityManager.persist(forecast);
        } else {
            // If the forecast ID is set, merge the forecast with the existing one
            forecast = entityManager.merge(forecast);
        }
        return forecast;
    }

}
