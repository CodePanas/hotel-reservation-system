package com.hotelreservation.repository;

import com.hotelreservation.model.Suite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuiteRepository extends MongoRepository<Suite, String> {
    // Buscar suites por tipo
    List<Suite> findByType(String type);

    // Buscar suites disponibles
    List<Suite> findByAvailable(boolean available);

    // Buscar suites por tipo y disponibilidad
    List<Suite> findByTypeAndAvailable(String type, boolean available);

    // Buscar suites por rango de precio
    List<Suite> findByPriceBetween(double minPrice, double maxPrice);
}