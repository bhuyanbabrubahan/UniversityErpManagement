package com.sms.student.report.scholarship.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipReportResponseDto {


    // Scholarship Information

    @Schema(description = "Scholarship ID")
    private Long scholarshipId;


    @Schema(description = "Scholarship name")
    private String scholarshipName;


    @Schema(description = "Scholarship type")
    private ScholarshipType scholarshipType;


    @Schema(description = "Current scholarship status")
    private ScholarshipStatus scholarshipStatus;



    // Student Information

    @Schema(description = "Student ID")
    private Long studentId;


    @Schema(description = "Student full name")
    private String studentName;


    @Schema(description = "Student roll number")
    private String rollNumber;



    // Admission Information

    @Schema(description = "Admission ID")
    private Long admissionId;


    @Schema(description = "Admission number")
    private String admissionNumber;


    @Schema(description = "Academic year")
    private String academicYear;



    // Academic Information

    @Schema(description = "Course name")
    private String courseName;


    @Schema(description = "Department name")
    private String departmentName;


    @Schema(description = "Semester name")
    private String semesterName;



    // Amount Information

    @Schema(description = "Requested scholarship amount")
    private BigDecimal requestedAmount;


    @Schema(description = "Approved scholarship amount")
    private BigDecimal approvedAmount;


    @Schema(description = "Final scholarship value")
    private BigDecimal scholarshipValue;



    // Approval Information

    @Schema(description = "Scholarship approval date")
    private LocalDateTime approvalDate;


    @Schema(description = "Approved by user name")
    private String approvedBy;

}