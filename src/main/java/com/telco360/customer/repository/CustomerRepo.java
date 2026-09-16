package com.telco360.customer.repository;

import com.telco360.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
}
