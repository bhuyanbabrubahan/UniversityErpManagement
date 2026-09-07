package com.sms.security.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.Gender;
import com.sms.security.enums.AdminDesignation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AdminUpdateRequest {

    private String firstName;
    private String lastName;

    private Gender gender;
    private LocalDate dateOfBirth;

    private AdminDesignation designation;
    private LocalDate joiningDate;
    private BigDecimal salary;

    private String email;
    private String mobileNumber;

    private String password;

    private String profileImage;
    private String remarks;

    @Valid
    private AddressRequestDto address;
}
