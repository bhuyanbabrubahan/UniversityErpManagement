package com.sms.fee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(
        name = "StudentFeeReceiptResponseDto",
        description = "Complete student fee receipt response containing receipt, student snapshot, payment, scholarship and audit details."
)
public class StudentFeeReceiptResponseDto {


    // =====================================================
    // Receipt Information
    // =====================================================

    private Long id;

    private String receiptNumber;

    private LocalDate receiptDate;

    private String remarks;



    // =====================================================
    // Payment Reference
    // =====================================================

    private Long paymentId;



    // =====================================================
    // Student Snapshot
    // =====================================================

    private Long studentId;

    private String studentName;

    private String rollNumber;



    // =====================================================
    // Admission Snapshot
    // =====================================================

    private Long admissionId;

    private String admissionNumber;

    private String academicYear;



    // =====================================================
    // Academic Snapshot
    // =====================================================

    private Long courseId;

    private String courseName;


    private Long departmentId;

    private String departmentName;


    private Long semesterId;

    private String semesterName;



    // =====================================================
    // Scholarship Snapshot
    // =====================================================

    private Long scholarshipId;

    private String scholarshipName;

    private ScholarshipType scholarshipType;

    private BigDecimal scholarshipAmount;



    // =====================================================
    // Fee Snapshot
    // =====================================================

    private BigDecimal totalFee;

    private BigDecimal finalPayableAmount;

    private BigDecimal paidAmount;

    private BigDecimal dueAmount;



    // =====================================================
    // Payment Snapshot
    // =====================================================

    private PaymentStatus paymentStatus;

    private PaymentMode paymentMode;

    private LocalDate paymentDate;

    private String transactionReference;



    // =====================================================
    // Record Status
    // =====================================================

    private RecordStatus status;



    // =====================================================
    // Generated Information
    // =====================================================

    private Long generatedBy;

    private String generatedByName;



    // =====================================================
    // Creation Audit
    // =====================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    private Long createdByUserId;

    private String createdBy;

    private LocalDateTime createdDate;



    // =====================================================
    // Print Audit
    // =====================================================

    private Long printedByUserId;

    private String printedBy;

    private LocalDateTime printedDate;



    // =====================================================
    // Delete Audit
    // =====================================================

    private Long deletedByUserId;

    private String deletedBy;

    private LocalDateTime deletedDate;

}