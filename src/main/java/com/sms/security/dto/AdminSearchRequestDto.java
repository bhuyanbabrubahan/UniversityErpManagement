package com.sms.security.dto;

import java.time.LocalDate;

import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.security.enums.AdminDesignation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AdminSearchRequestDto", description = "Search filters for admin records.")
public class AdminSearchRequestDto {

    // IDENTITY

    private Long id;

    private String employeeCode;

    // PERSONAL INFORMATION

    private String firstName;

    private String lastName;

    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirthFrom;

    private LocalDate dateOfBirthTo;

    // EMPLOYMENT INFORMATION

    private AdminDesignation designation;

    private LocalDate joiningDateFrom;

    private LocalDate joiningDateTo;

    // ACCOUNT INFORMATION

    private String email;

    private String mobileNumber;

    // ADDRESS INFORMATION

    private Long villageId;

    private Long tehsilId;

    private Long districtId;

    private Long stateId;

    private Long countryId;

    private String village;

    private String district;

    private String state;

    private String country;

    private String pincode;

    // RECORD STATUS

    private RecordStatus status;
}
