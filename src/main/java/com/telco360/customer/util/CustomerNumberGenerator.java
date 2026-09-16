package com.telco360.customer.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CustomerNumberGenerator {
    private final AtomicLong counter = new AtomicLong(1);

    public String generate(){
        return String.format("CUST%06d",counter.getAndIncrement());
    }

}
