package com.hotelreservation.controller;

import com.hotelreservation.model.Reservation;
import com.hotelreservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Obtener todas las reservaciones
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // Obtener una reservación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener reservaciones por cliente
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomer(customerId));
    }

    // Obtener reservaciones por suite
    @GetMapping("/suite/{suiteId}")
    public ResponseEntity<List<Reservation>> getReservationsBySuite(@PathVariable String suiteId) {
        return ResponseEntity.ok(reservationService.getReservationsBySuite(suiteId));
    }

    // Obtener reservaciones activas
    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        return ResponseEntity.ok(reservationService.getActiveReservations());
    }

    // Obtener reservaciones por rango de fechas
    @GetMapping("/date-range")
    public ResponseEntity<List<Reservation>> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reservationService.getReservationsByDateRange(startDate, endDate));
    }

    // Crear una nueva reservación
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            logger.info("Creating reservation for customer: {} and suite: {}",
                    reservation.getCustomer().getId(), reservation.getSuite().getId());

            if (reservation.getCustomer() == null || reservation.getCustomer().getId() == null) {
                logger.error("Customer ID not provided");
                return ResponseEntity.badRequest().body("Customer ID not provided");
            }

            if (reservation.getSuite() == null || reservation.getSuite().getId() == null) {
                logger.error("Suite ID not provided");
                return ResponseEntity.badRequest().body("Suite ID not provided");
            }

            Reservation newReservation = reservationService.createReservation(reservation);
            logger.info("Reservation created successfully with ID: {}", newReservation.getId());
            return ResponseEntity.ok(newReservation);
        } catch (RuntimeException e) {
            logger.error("Error creating reservation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error creating reservation", e);
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }

    // Actualizar una reservación existente
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable String id,
            @RequestBody Reservation reservation) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, reservation);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Cancelar una reservación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}