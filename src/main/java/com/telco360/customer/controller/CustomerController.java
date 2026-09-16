package com.telco360.customer.controller;

import com.telco360.customer.dto.request.CustomerRequest;
import com.telco360.customer.dto.request.CustomerUpdateRequest;
import com.telco360.customer.dto.response.CustomerResponse;
import com.telco360.customer.dto.response.PageResponse;
import com.telco360.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
        CustomerResponse response = service.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = service.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        PageResponse<CustomerResponse> response = service.getAllCustomers(page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        CustomerResponse response = service.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealthStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "Customer service is running.");
        return ResponseEntity.ok(status);
    }


}
