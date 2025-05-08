package com.hotelreservation.controller;

import com.hotelreservation.model.Suite;
import com.hotelreservation.service.SuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/suites")
@CrossOrigin(origins = "*")
@Tag(name = "Suite", description = "API de gestión de suites")
public class SuiteController {

    private final SuiteService suiteService;

    @Autowired
    public SuiteController(SuiteService suiteService) {
        this.suiteService = suiteService;
    }

    @Operation(summary = "Obtener todas las suites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de suites encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Suite>> getAllSuites() {
        return ResponseEntity.ok(suiteService.getAllSuites());
    }

    @Operation(summary = "Obtener una suite por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suite encontrada"),
            @ApiResponse(responseCode = "404", description = "Suite no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Suite> getSuiteById(
            @Parameter(description = "ID de la suite") @PathVariable String id) {
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

    @Operation(summary = "Crear una nueva suite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suite creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la suite inválidos")
    })
    @PostMapping
    public ResponseEntity<Suite> createSuite(
            @Parameter(description = "Datos de la suite") @RequestBody Suite suite) {
        return ResponseEntity.ok(suiteService.createSuite(suite));
    }

    @Operation(summary = "Actualizar una suite existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suite actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suite no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Suite> updateSuite(
            @Parameter(description = "ID de la suite") @PathVariable String id,
            @Parameter(description = "Datos actualizados de la suite") @RequestBody Suite suite) {
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

    @Operation(summary = "Eliminar una suite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suite eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suite no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuite(
            @Parameter(description = "ID de la suite") @PathVariable String id) {
        try {
            suiteService.deleteSuite(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}