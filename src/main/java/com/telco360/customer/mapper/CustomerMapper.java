package com.telco360.customer.mapper;

import com.telco360.customer.dto.request.CustomerRequest;
import com.telco360.customer.dto.response.CustomerResponse;
import com.telco360.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request){
        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        return customer;
    }

    public CustomerResponse toResponse(Customer customer){
        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setMobileNumber(customer.getMobileNumber());
        response.setDateOfBirth(customer.getDateOfBirth());
        response.setStatus(customer.getStatus());
        response.setCustomerNumber(customer.getCustomerNumber());
        return response;
    }
}