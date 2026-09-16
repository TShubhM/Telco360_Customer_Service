package com.telco360.customer.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.telco360.customer.entity.Customer;
import com.telco360.customer.repository.CustomerRepo;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CustomerNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public CustomerNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generate() {
        Long number = jdbcTemplate.queryForObject(
                "SELECT nextval('customer_number_seq')",
                Long.class
        );
        return String.format("CUST%06d", number);
    }


//    Previous Approach
//    private final AtomicLong counter;

    //finding maxId would load all customers into memory just to find the highest ID
//    And in real microservice deployment two pods could generate same customer ID
    /*public CustomerNumberGenerator(CustomerRepo repo){
        long maxId = repo.findAll()
                .stream()
                .mapToLong(Customer::getId)
                .max()
                .orElse(0);
        counter = new AtomicLong(maxId + 1);
    }

    public String generate(){
        return String.format("CUST%06d",counter.getAndIncrement());
    }*/


}
