package com.telco360.customer.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CustomerUpdateRequest {

    @NotBlank(message = "First Name is required.")
    private String firstName;

    @NotBlank(message = "Last Name is required.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile Number is Required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian mobile number"
    )
    private String mobileNumber;

    @NotNull(message = "Date of Birth is required.")
    @Past(message = "Date of Birth must be in the past")
    private LocalDate dateOfBirth;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
