package com.sms.fee.report.dto;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "CourseFeeReportDto",
        description = "DTO containing course wise fee collection report details"
)
public class CourseWiseReportDto {



    // =====================================================
    // Course Information
    // =====================================================


    @Schema(
            description = "Course unique identifier",
            example = "1"
    )
    private Long courseId;



    @Schema(
            description = "Course name",
            example = "B.Tech Information Technology"
    )
    private String courseName;



    @Schema(
            description = "Department name associated with course",
            example = "Information Technology"
    )
    private String departmentName;



    // =====================================================
    // Student Statistics
    // =====================================================


    @Schema(
            description = "Total number of students enrolled in course",
            example = "60"
    )
    private Long totalStudents;



    // =====================================================
    // Fee Summary
    // =====================================================


    @Schema(
            description = "Total fee amount assigned for course students",
            example = "3480000.00"
    )
    private BigDecimal totalFee;



    @Schema(
            description = "Total fee collected from students",
            example = "3200000.00"
    )
    private BigDecimal collectedAmount;



    @Schema(
            description = "Remaining pending fee amount",
            example = "280000.00"
    )
    private BigDecimal pendingAmount;



}