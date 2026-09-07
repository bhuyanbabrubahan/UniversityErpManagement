package com.sms.fee.report.dto;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "AcademicYearFeeReportDto",
        description = "DTO containing academic year wise fee collection report details"
)
public class AcademicYearWiseFeeReportDto {



    // =====================================================
    // Academic Year Information
    // =====================================================


    @Schema(
            description = "Academic year",
            example = "2026-2027"
    )
    private String academicYear;



    // =====================================================
    // Student Statistics
    // =====================================================


    @Schema(
            description = "Total number of students admitted in academic year",
            example = "500"
    )
    private Long totalStudents;



    // =====================================================
    // Fee Summary
    // =====================================================


    @Schema(
            description = "Total fee amount generated for academic year",
            example = "29000000.00"
    )
    private BigDecimal totalFee;



    @Schema(
            description = "Total fee collected during academic year",
            example = "25000000.00"
    )
    private BigDecimal collectedAmount;



    @Schema(
            description = "Total pending fee amount for academic year",
            example = "4000000.00"
    )
    private BigDecimal pendingAmount;



    // =====================================================
    // Payment Statistics
    // =====================================================


    @Schema(
            description = "Total number of fee payment transactions",
            example = "1500"
    )
    private Long totalPayments;



}