package com.telco360.customer.service;

import com.telco360.customer.dto.request.CustomerRequest;
import com.telco360.customer.dto.response.CustomerResponse;
import com.telco360.customer.entity.Customer;
import com.telco360.customer.exception.CustomerAlreadyExistsException;
import com.telco360.customer.exception.CustomerNotFoundException;
import com.telco360.customer.mapper.CustomerMapper;
import com.telco360.customer.repository.CustomerRepo;
import com.telco360.customer.util.CustomerNumberGenerator;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepo repo;
    private final CustomerMapper mapper;
    private final CustomerNumberGenerator generator;

    public CustomerService(CustomerRepo repo, CustomerMapper mapper, CustomerNumberGenerator generator) {
        this.repo = repo;
        this.mapper = mapper;
        this.generator = generator;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with this Email already exists.");
        }

        if (repo.existsByMobileNumber(request.getMobileNumber())) {
            throw new CustomerAlreadyExistsException("Customer with this mobile number already exists.");
        }

        Customer customer = mapper.toEntity(request);

        customer.setCustomerNumber(generator.generate());
        customer.setStatus("ACTIVE");

        Customer savedCustomer = repo.save(customer);

        return mapper.toResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id : " + id));
        return mapper.toResponse(customer);
    }
}
