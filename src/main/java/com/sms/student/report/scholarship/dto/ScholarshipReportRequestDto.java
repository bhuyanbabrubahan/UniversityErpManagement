package com.sms.student.report.scholarship.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipReportRequestDto {


    // Student Filter

    private Long studentId;

    private String rollNumber;

    private String studentName;



    // Admission Filter

    private Long admissionId;

    private String admissionNumber;



    // Academic Filter

    private String academicYear;

    private Long courseId;

    private String courseName;

    private Long departmentId;

    private String departmentName;

    private Long semesterId;

    private String semesterName;



    // Scholarship Filter

    private String scholarshipName;

    private ScholarshipType scholarshipType;

    private ScholarshipStatus scholarshipStatus;



    // Amount Filter

    private BigDecimal minRequestedAmount;

    private BigDecimal maxRequestedAmount;

    private BigDecimal minApprovedAmount;

    private BigDecimal maxApprovedAmount;



    // Approval Date Filter

    private LocalDate approvalDateFrom;

    private LocalDate approvalDateTo;



    // Record Status Filter

    private RecordStatus status;

}