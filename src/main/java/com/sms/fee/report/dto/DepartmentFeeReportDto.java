package com.sms.fee.report.dto;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "DepartmentFeeReportDto",
        description = "DTO containing department wise fee collection report details"
)
public class DepartmentFeeReportDto {



    // =====================================================
    // Department Information
    // =====================================================


    @Schema(
            description = "Department unique identifier",
            example = "1"
    )
    private Long departmentId;



    @Schema(
            description = "Department name",
            example = "Information Technology"
    )
    private String departmentName;




    // =====================================================
    // Student Statistics
    // =====================================================


    @Schema(
            description = "Total number of students in department",
            example = "120"
    )
    private Long totalStudents;




    // =====================================================
    // Fee Summary
    // =====================================================


    @Schema(
            description = "Total fee assigned for department students",
            example = "5800000.00"
    )
    private BigDecimal totalFee;



    @Schema(
            description = "Total collected fee amount",
            example = "5200000.00"
    )
    private BigDecimal collectedAmount;



    @Schema(
            description = "Total pending fee amount",
            example = "600000.00"
    )
    private BigDecimal pendingAmount;



}