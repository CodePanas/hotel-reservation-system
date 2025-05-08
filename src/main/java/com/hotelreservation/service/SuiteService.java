package com.hotelreservation.service;

import com.hotelreservation.model.Suite;
import com.hotelreservation.repository.SuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuiteService {

    private final SuiteRepository suiteRepository;

    @Autowired
    public SuiteService(SuiteRepository suiteRepository) {
        this.suiteRepository = suiteRepository;
    }

    // Obtener todas las suites
    public List<Suite> getAllSuites() {
        return suiteRepository.findAll();
    }

    // Obtener una suite por ID
    public Optional<Suite> getSuiteById(String id) {
        return suiteRepository.findById(id);
    }

    // Obtener suites por tipo
    public List<Suite> getSuitesByType(String type) {
        return suiteRepository.findByType(type);
    }

    // Obtener suites disponibles
    public List<Suite> getAvailableSuites() {
        return suiteRepository.findByAvailable(true);
    }

    // Obtener suites por tipo y disponibilidad
    public List<Suite> getSuitesByTypeAndAvailability(String type, boolean available) {
        return suiteRepository.findByTypeAndAvailable(type, available);
    }

    // Obtener suites por rango de precio
    public List<Suite> getSuitesByPriceRange(double minPrice, double maxPrice) {
        return suiteRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Crear una nueva suite
    public Suite createSuite(Suite suite) {
        return suiteRepository.save(suite);
    }

    // Actualizar una suite existente
    public Suite updateSuite(String id, Suite suiteDetails) {
        return suiteRepository.findById(id)
                .map(existingSuite -> {
                    existingSuite.setType(suiteDetails.getType());
                    existingSuite.setPrice(suiteDetails.getPrice());
                    existingSuite.setAvailable(suiteDetails.isAvailable());
                    return suiteRepository.save(existingSuite);
                })
                .orElseThrow(() -> new RuntimeException("Suite no encontrada con id: " + id));
    }

    // Actualizar disponibilidad de una suite
    public Suite updateAvailability(String id, boolean available) {
        return suiteRepository.findById(id)
                .map(suite -> {
                    suite.setAvailable(available);
                    return suiteRepository.save(suite);
                })
                .orElseThrow(() -> new RuntimeException("Suite no encontrada con id: " + id));
    }

    // Eliminar una suite
    public void deleteSuite(String id) {
        if (!suiteRepository.existsById(id)) {
            throw new RuntimeException("Suite no encontrada con id: " + id);
        }
        suiteRepository.deleteById(id);
    }
}