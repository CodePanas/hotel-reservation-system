package com.hotelreservation.service;

import com.hotelreservation.model.Customer;
import com.hotelreservation.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Obtener todos los clientes
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Obtener un cliente por ID
    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }

    // Obtener un cliente por email
    public Optional<Customer> getCustomerByEmail(String email) {
        return Optional.ofNullable(customerRepository.findByEmail(email));
    }

    // Crear un nuevo cliente
    public Customer createCustomer(Customer customer) {
        // Verificar si el email ya existe
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        return customerRepository.save(customer);
    }

    // Actualizar un cliente existente
    public Customer updateCustomer(String id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDetails.getName());
                    existingCustomer.setEmail(customerDetails.getEmail());
                    existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    // Eliminar un cliente
    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        customerRepository.deleteById(id);
    }
}