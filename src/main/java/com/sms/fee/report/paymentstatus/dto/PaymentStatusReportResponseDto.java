package com.sms.fee.report.paymentstatus.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Payment Status Report Response")
public class PaymentStatusReportResponseDto {

    // =====================================================
    // Payment
    // =====================================================

    @Schema(description = "Payment Id", example = "1")
    private Long paymentId;

    // =====================================================
    // Student
    // =====================================================

    @Schema(description = "Student Id", example = "1")
    private Long studentId;

    @Schema(description = "Student Name", example = "Rahul Kumar Sharma")
    private String studentName;

    @Schema(description = "Roll Number", example = "STU20260001")
    private String rollNumber;

    // =====================================================
    // Admission
    // =====================================================

    @Schema(description = "Admission Id", example = "1")
    private Long admissionId;

    @Schema(description = "Admission Number", example = "ADM20260001")
    private String admissionNumber;

    // =====================================================
    // Academic
    // =====================================================

    @Schema(description = "Academic Year", example = "2026-2027")
    private String academicYear;

    @Schema(description = "Course Name")
    private String courseName;

    @Schema(description = "Department Name")
    private String departmentName;

    @Schema(description = "Semester Name")
    private String semesterName;

    // =====================================================
    // Fee Details
    // =====================================================

    @Schema(description = "Total Fee", example = "60000")
    private BigDecimal totalFee;

    @Schema(description = "Scholarship Amount", example = "15000")
    private BigDecimal scholarshipAmount;

    @Schema(description = "Final Payable Amount", example = "45000")
    private BigDecimal finalPayableAmount;

    @Schema(description = "Paid Amount", example = "30000")
    private BigDecimal paidAmount;

    @Schema(description = "Due Amount", example = "15000")
    private BigDecimal dueAmount;

    // =====================================================
    // Payment Details
    // =====================================================

    @Schema(description = "Payment Status")
    private PaymentStatus paymentStatus;

    @Schema(description = "Payment Mode")
    private PaymentMode paymentMode;

    @Schema(description = "Payment Date")
    private LocalDate paymentDate;

    @Schema(description = "Transaction Reference")
    private String transactionReference;

}