package com.hotelreservation.service;

import com.hotelreservation.model.Customer;
import com.hotelreservation.model.Reservation;
import com.hotelreservation.model.Suite;
import com.hotelreservation.repository.CustomerRepository;
import com.hotelreservation.repository.ReservationRepository;
import com.hotelreservation.repository.SuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final SuiteRepository suiteRepository;

    @Autowired
    public ReservationService(
            ReservationRepository reservationRepository,
            CustomerRepository customerRepository,
            SuiteRepository suiteRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.suiteRepository = suiteRepository;
    }

    // Obtener todas las reservaciones
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Obtener una reservación por ID
    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }

    // Obtener reservaciones por cliente
    public List<Reservation> getReservationsByCustomer(String customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    // Obtener reservaciones por suite
    public List<Reservation> getReservationsBySuite(String suiteId) {
        return reservationRepository.findBySuiteId(suiteId);
    }

    // Obtener reservaciones activas
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByCheckOutDateAfter(LocalDate.now());
    }

    // Obtener reservaciones por rango de fechas
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                endDate, startDate);
    }

    // Crear una nueva reservación
    public Reservation createReservation(Reservation reservation) {
        // Validate customer exists
        Customer customer = customerRepository.findById(reservation.getCustomer().getId())
                .orElseThrow(() -> {
                    logger.error("Customer not found with ID: {}", reservation.getCustomer().getId());
                    return new RuntimeException("Customer not found");
                });

        // Validate suite exists and is available
        Suite suite = suiteRepository.findById(reservation.getSuite().getId())
                .orElseThrow(() -> {
                    logger.error("Suite not found with ID: {}", reservation.getSuite().getId());
                    return new RuntimeException("Suite not found");
                });

        if (!suite.isAvailable()) {
            logger.error("Suite {} is not available", suite.getId());
            throw new RuntimeException("Suite is not available");
        }

        // Validate dates
        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate())) {
            logger.error("Invalid dates: check-in {} is after check-out {}",
                    reservation.getCheckInDate(), reservation.getCheckOutDate());
            throw new RuntimeException("Check-in date must be before check-out date");
        }

        if (reservation.getCheckInDate().isBefore(LocalDate.now())) {
            logger.error("Check-in date {} is in the past", reservation.getCheckInDate());
            throw new RuntimeException("Check-in date cannot be in the past");
        }

        // Check suite availability for selected dates
        boolean isSuiteBooked = reservationRepository
                .existsBySuiteIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                        suite.getId(), reservation.getCheckOutDate(), reservation.getCheckInDate());

        if (isSuiteBooked) {
            logger.error("Suite {} is already booked for dates {} - {}",
                    suite.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate());
            throw new RuntimeException("Suite is already booked for selected dates");
        }

        // Update suite availability
        suite.setAvailable(false);
        suiteRepository.save(suite);

        // Save reservation
        Reservation savedReservation = reservationRepository.save(reservation);
        logger.info("Reservation created successfully with ID: {}", savedReservation.getId());

        return savedReservation;
    }

    // Actualizar una reservación existente
    public Reservation updateReservation(String id, Reservation reservationDetails) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    // Validar fechas
                    if (reservationDetails.getCheckInDate().isAfter(reservationDetails.getCheckOutDate())) {
                        throw new RuntimeException("La fecha de entrada debe ser anterior a la fecha de salida");
                    }

                    // Actualizar fechas
                    existingReservation.setCheckInDate(reservationDetails.getCheckInDate());
                    existingReservation.setCheckOutDate(reservationDetails.getCheckOutDate());

                    return reservationRepository.save(existingReservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada con id: " + id));
    }

    // Cancelar una reservación
    public void cancelReservation(String id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservación no encontrada con id: " + id));

        // Liberar la suite
        Suite suite = reservation.getSuite();
        suite.setAvailable(true);
        suiteRepository.save(suite);

        // Eliminar la reservación
        reservationRepository.deleteById(id);
    }
}