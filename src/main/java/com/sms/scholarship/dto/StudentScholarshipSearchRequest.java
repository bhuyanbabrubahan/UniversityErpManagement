package com.sms.scholarship.dto;


import java.math.BigDecimal;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class StudentScholarshipSearchRequest {



    // =====================================================
    // Student Filter
    // =====================================================


    @Schema(
            description = "Search by student id",
            example = "1"
    )
    private Long studentId;





    // =====================================================
    // Admission Filter
    // =====================================================


    @Schema(
            description = "Search by admission id",
            example = "10"
    )
    private Long admissionId;





    // =====================================================
    // Academic Year
    // =====================================================


    @Schema(
            description = "Academic year",
            example = "2026-2027"
    )
    private String academicYear;





    // =====================================================
    // Scholarship Name
    // =====================================================


    @Size(
            max = 100,
            message = "Scholarship name cannot exceed 100 characters"
    )
    @Schema(
            description = "Scholarship name search",
            example = "Merit Scholarship"
    )
    private String scholarshipName;





    // =====================================================
    // Scholarship Type
    // =====================================================


    @Schema(
            description = "Scholarship type",
            example = "PERCENTAGE"
    )
    private ScholarshipType scholarshipType;





    // =====================================================
    // Minimum Approved Amount
    // =====================================================


    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Minimum approved amount cannot be negative"
    )
    @Schema(
            description = "Minimum approved scholarship amount",
            example = "5000"
    )
    private BigDecimal minApprovedAmount;





    // =====================================================
    // Maximum Approved Amount
    // =====================================================


    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Maximum approved amount cannot be negative"
    )
    @Schema(
            description = "Maximum approved scholarship amount",
            example = "50000"
    )
    private BigDecimal maxApprovedAmount;





    // =====================================================
    // Status
    // =====================================================


    @Schema(
            description = "Record status",
            example = "ACTIVE"
    )
    private RecordStatus status;

    
    @Schema(
            description = "Scholarship status",
            example = "PENDING"
    )
    private ScholarshipStatus scholarshipStatus;

}