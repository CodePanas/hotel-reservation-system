package com.hotelreservation.repository;

import com.hotelreservation.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    // Buscar reservaciones por cliente
    List<Reservation> findByCustomerId(String customerId);

    // Buscar reservaciones por suite
    List<Reservation> findBySuiteId(String suiteId);

    // Buscar reservaciones activas (fecha de salida posterior a hoy)
    List<Reservation> findByCheckOutDateAfter(LocalDate date);

    // Buscar reservaciones por rango de fechas
    List<Reservation> findByCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            LocalDate checkOutDate, LocalDate checkInDate);

    // Verificar si existe una reservación para una suite en un rango de fechas
    boolean existsBySuiteIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            String suiteId, LocalDate checkOutDate, LocalDate checkInDate);
}