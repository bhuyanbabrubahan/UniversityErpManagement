package com.sms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "StudentSearchRequest",
        description = "Request DTO used to search students using optional filters."
)
public class StudentSearchRequest {

    // ==========================================================
    // Student
    // ==========================================================

    @Schema(
            description = "Student ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Student Roll Number",
            example = "STU20260001"
    )
    private String rollNumber;

    @Schema(
            description = "First Name (partial search supported)",
            example = "Rahul"
    )
    private String firstName;

    @Schema(
            description = "Last Name (partial search supported)",
            example = "Sharma"
    )
    private String lastName;

    @Schema(
            description = "Full Name (partial search supported)",
            example = "Rahul Sharma"
    )
    private String fullName;


    @Schema(
            description = "Gender",
            example = "MALE"
    )
    private Gender gender;

    // ==========================================================
    // Department
    // ==========================================================

    @Schema(
            description = "Department ID",
            example = "2"
    )
    private Long departmentId;

    // ==========================================================
    // Address
    // ==========================================================

    @Schema(
            description = "Village Name",
            example = "Gangadahani"
    )
    private String village;

    @Schema(
            description = "Tehsil Name",
            example = "Purushottampur"
    )
    private String tehsil;

    @Schema(
            description = "District Name",
            example = "Ganjam"
    )
    private String district;

    @Schema(
            description = "State Name",
            example = "Odisha"
    )
    private String state;

    @Schema(
            description = "Country Name",
            example = "India"
    )
    private String country;

    @Schema(
            description = "Pincode",
            example = "761018"
    )
    private String pincode;

    // ==========================================================
    // Admission Date Range
    // ==========================================================

    @Schema(
            description = "Admission Date From",
            example = "2026-01-01"
    )
    private LocalDate admissionDateFrom;

    @Schema(
            description = "Admission Date To",
            example = "2026-12-31"
    )
    private LocalDate admissionDateTo;

    // ==========================================================
    // Date Of Birth Range
    // ==========================================================

    @Schema(
            description = "Date Of Birth From",
            example = "2000-01-01"
    )
    private LocalDate dateOfBirthFrom;

    @Schema(
            description = "Date Of Birth To",
            example = "2005-12-31"
    )
    private LocalDate dateOfBirthTo;

    // ==========================================================
    // Fee Range
    // ==========================================================

    @Schema(
            description = "Minimum Fees",
            example = "10000"
    )
    private BigDecimal minFees;

    @Schema(
            description = "Maximum Fees",
            example = "50000"
    )
    private BigDecimal maxFees;

    // ==========================================================
    // Status
    // ==========================================================

    @Schema(
            description = "Record Status",
            example = "ACTIVE"
    )
    private RecordStatus status;

}