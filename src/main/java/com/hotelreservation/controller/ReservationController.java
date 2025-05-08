package com.hotelreservation.controller;

import com.hotelreservation.model.Reservation;
import com.hotelreservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
@Tag(name = "Reservation", description = "API de gestión de reservaciones")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Obtener todas las reservaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservaciones encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @Operation(summary = "Obtener una reservación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservación encontrada"),
            @ApiResponse(responseCode = "404", description = "Reservación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @Parameter(description = "ID de la reservación") @PathVariable String id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reservaciones por cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservaciones del cliente encontrada"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomer(
            @Parameter(description = "ID del cliente") @PathVariable String customerId) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomer(customerId));
    }

    @Operation(summary = "Obtener reservaciones por suite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservaciones de la suite encontrada"),
            @ApiResponse(responseCode = "404", description = "Suite no encontrada")
    })
    @GetMapping("/suite/{suiteId}")
    public ResponseEntity<List<Reservation>> getReservationsBySuite(
            @Parameter(description = "ID de la suite") @PathVariable String suiteId) {
        return ResponseEntity.ok(reservationService.getReservationsBySuite(suiteId));
    }

    @Operation(summary = "Crear una nueva reservación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de reservación inválidos")
    })
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Parameter(description = "Datos de la reservación") @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.createReservation(reservation));
    }

    @Operation(summary = "Actualizar una reservación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reservación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @Parameter(description = "ID de la reservación") @PathVariable String id,
            @Parameter(description = "Datos actualizados de la reservación") @RequestBody Reservation reservation) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, reservation);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cancelar una reservación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservación cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reservación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @Parameter(description = "ID de la reservación") @PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}