package com.telco360.customer.service;

import com.telco360.customer.dto.request.CustomerRequest;
import com.telco360.customer.dto.request.CustomerUpdateRequest;
import com.telco360.customer.dto.response.CustomerResponse;
import com.telco360.customer.dto.response.PageResponse;
import com.telco360.customer.entity.Customer;
import com.telco360.customer.exception.CustomerAlreadyExistsException;
import com.telco360.customer.exception.CustomerNotFoundException;
import com.telco360.customer.exception.InvalidRequestException;
import com.telco360.customer.mapper.CustomerMapper;
import com.telco360.customer.repository.CustomerRepo;
import com.telco360.customer.util.CustomerNumberGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    private final CustomerRepo repo;
    private final CustomerMapper mapper;
    private final CustomerNumberGenerator generator;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "customerNumber", "firstName", "lastName", "email", "createdAt"
    );
    private static final Set<String> ALLOWED_SORT_DIRECTION = Set.of(
            "asc", "desc"
    );

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
//        System.out.println("Generated customer number " + customer.getCustomerNumber());

        return mapper.toResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id : " + id));
        return mapper.toResponse(customer);
    }

    public PageResponse<CustomerResponse> getAllCustomers(int page, int size, String sortBy, String direction) {
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        sortBy = sortBy.trim();
        direction = direction.trim().toLowerCase();
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidRequestException("Invalid Sort Field " + sortBy);
        }

        if (!ALLOWED_SORT_DIRECTION.contains(direction)) {
            throw new InvalidRequestException("Invalid Sort Direction " + direction);
        }
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,
                size,
                sort);
        Page<Customer> customers = repo.findAll(pageable);

        List<CustomerResponse> content = customers.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        PageResponse<CustomerResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(customers.getNumber());
        response.setSize(customers.getSize());
        response.setTotalElements(customers.getTotalElements());
        response.setTotalPages(customers.getTotalPages());
        response.setLast(customers.isLast());
        return response;
    }

    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        //Find existing Customer
        Customer customer = repo.findById(id).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found with id " + id));

        //Check email belongs to another customer
        if (repo.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new CustomerAlreadyExistsException("Customer with this email already exists.");
        }
        // check mobile number belong to another customer
        if (repo.existsByMobileNumberAndIdNot(request.getMobileNumber(), id)) {
            throw new CustomerAlreadyExistsException("Customer with this mobile number already exists.");
        }

        //update allowed fields
        mapper.updateEntity(customer, request);
        //save updated customer
        Customer updatedCustomer = repo.save(customer);
        //convert entity to response DTO
        return mapper.toResponse(updatedCustomer);

    }
}
