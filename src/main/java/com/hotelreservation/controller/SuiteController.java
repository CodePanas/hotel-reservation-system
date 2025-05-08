package com.hotelreservation.controller;

import com.hotelreservation.model.Suite;
import com.hotelreservation.service.SuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suites")
@CrossOrigin(origins = "*")
public class SuiteController {

    private final SuiteService suiteService;

    @Autowired
    public SuiteController(SuiteService suiteService) {
        this.suiteService = suiteService;
    }

    // Obtener todas las suites
    @GetMapping
    public ResponseEntity<List<Suite>> getAllSuites() {
        return ResponseEntity.ok(suiteService.getAllSuites());
    }

    // Obtener una suite por ID
    @GetMapping("/{id}")
    public ResponseEntity<Suite> getSuiteById(@PathVariable String id) {
        return suiteService.getSuiteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener suites por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Suite>> getSuitesByType(@PathVariable String type) {
        return ResponseEntity.ok(suiteService.getSuitesByType(type));
    }

    // Obtener suites disponibles
    @GetMapping("/available")
    public ResponseEntity<List<Suite>> getAvailableSuites() {
        return ResponseEntity.ok(suiteService.getAvailableSuites());
    }

    // Obtener suites por tipo y disponibilidad
    @GetMapping("/type/{type}/available/{available}")
    public ResponseEntity<List<Suite>> getSuitesByTypeAndAvailability(
            @PathVariable String type,
            @PathVariable boolean available) {
        return ResponseEntity.ok(suiteService.getSuitesByTypeAndAvailability(type, available));
    }

    // Obtener suites por rango de precio
    @GetMapping("/price-range")
    public ResponseEntity<List<Suite>> getSuitesByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return ResponseEntity.ok(suiteService.getSuitesByPriceRange(minPrice, maxPrice));
    }

    // Crear una nueva suite
    @PostMapping
    public ResponseEntity<Suite> createSuite(@RequestBody Suite suite) {
        try {
            Suite newSuite = suiteService.createSuite(suite);
            return ResponseEntity.ok(newSuite);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar una suite existente
    @PutMapping("/{id}")
    public ResponseEntity<Suite> updateSuite(@PathVariable String id, @RequestBody Suite suite) {
        try {
            Suite updatedSuite = suiteService.updateSuite(id, suite);
            return ResponseEntity.ok(updatedSuite);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar disponibilidad de una suite
    @PatchMapping("/{id}/availability")
    public ResponseEntity<Suite> updateAvailability(
            @PathVariable String id,
            @RequestParam boolean available) {
        try {
            Suite updatedSuite = suiteService.updateAvailability(id, available);
            return ResponseEntity.ok(updatedSuite);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una suite
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuite(@PathVariable String id) {
        try {
            suiteService.deleteSuite(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}