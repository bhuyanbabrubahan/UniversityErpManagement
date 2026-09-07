package com.sms.fee.report.pendingdue.dto;

import java.math.BigDecimal;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * =====================================================
 * Pending Due Report Response DTO
 * =====================================================
 *
 * Purpose:
 * Shows students having pending fee dues.
 *
 * Used For:
 * - Pending Fee Due Report
 * - Student wise outstanding fee tracking
 * - Admin fee follow-up
 *
 * =====================================================
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Pending Fee Due Report Response"
)
public class PendingDueReportResponseDto {


    // =====================================================
    // Student Information
    // =====================================================


    @Schema(
            description = "Student ID",
            example = "1"
    )
    private Long studentId;



    @Schema(
            description = "Student Name",
            example = "Rahul Kumar Sharma"
    )
    private String studentName;



    @Schema(
            description = "Roll Number",
            example = "STU20260007"
    )
    private String rollNumber;



    // =====================================================
    // Admission Information
    // =====================================================


    @Schema(
            description = "Admission Number",
            example = "ADM20260001"
    )
    private String admissionNumber;



    @Schema(
            description = "Academic Year",
            example = "2026-2027"
    )
    private String academicYear;



    // =====================================================
    // Academic Information
    // =====================================================


    @Schema(
            description = "Department Name",
            example = "Information Technology"
    )
    private String departmentName;



    @Schema(
            description = "Course Name",
            example = "Information Technology"
    )
    private String courseName;



    // =====================================================
    // Fee Information
    // =====================================================


    @Schema(
            description = "Total Fee",
            example = "58000.00"
    )
    private BigDecimal totalFee;



    @Schema(
            description = "Scholarship Amount",
            example = "14500.00"
    )
    private BigDecimal scholarshipAmount;



    @Schema(
            description = "Final Payable Amount",
            example = "43500.00"
    )
    private BigDecimal finalPayableAmount;



    @Schema(
            description = "Paid Amount",
            example = "18800.00"
    )
    private BigDecimal paidAmount;



    @Schema(
            description = "Pending Due Amount",
            example = "24700.00"
    )
    private BigDecimal dueAmount;

    @Schema(
            description = "Payment Status",
            example = "PENDING"
    )
    private PaymentStatus paymentStatus;
    

    @Schema(
            description = "Record Status",
            example = "ACTIVE"
    )
    private RecordStatus status;
}