package com.hotelreservation.repository;

import com.hotelreservation.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    // Método para buscar por email
    Customer findByEmail(String email);

    // Método para verificar si existe un email
    boolean existsByEmail(String email);
}