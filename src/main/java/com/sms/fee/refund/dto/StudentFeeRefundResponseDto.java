package com.sms.fee.refund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.fee.refund.enums.RefundMode;
import com.sms.fee.refund.enums.RefundStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "StudentFeeRefundResponseDto",
    description = "Response DTO containing complete student fee refund details"
)
public class StudentFeeRefundResponseDto {

    // Refund Identity

    @Schema(description = "Unique refund identifier", 
    		example = "1")
    private Long id;

    @Schema(description = "System generated unique refund number", 
    		example = "SMS-REF-20260721-000001")
    private String refundNumber;

    // Payment Details

    @Schema(description = "Payment identifier", 
    		example = "101")
    private Long paymentId;

    // Student Details

    @Schema(description = "Student identifier", 
    		example = "1001")
    private Long studentId;

    @Schema(description = "Student full name", 
    		example = "Rahul Sharma")
    private String studentName;

    @Schema(description = "Student roll number", 
    		example = "2026CS001")
    private String rollNumber;

    // Admission Details

    @Schema(description = "Admission identifier", 
    		example = "501")
    private Long admissionId;

    @Schema(description = "Admission number", 
    		example = "ADM-2026-0001")
    private String admissionNumber;

    // Refund Details

    @Schema(description = "Approved refund amount", 
    		example = "2500.00")
    private BigDecimal refundAmount;

    @Schema(description = "Reason provided for refund", 
    		example = "Duplicate fee payment")
    private String refundReason;

    @Schema(description = "Date on which refund was initiated", 
    		example = "2026-07-23")
    private LocalDate refundDate;

    @Schema(description = "Refund payment mode", 
    		example = "BANK_TRANSFER")
    private RefundMode refundMode;

    @Schema(description = "Current refund processing status", 
    		example = "APPROVED")
    private RefundStatus refundStatus;

    // Original Payment Information

    @Schema(description = "Original payment transaction reference", 
    		example = "TXN987654321")
    private String paymentTransactionReference;

    @Schema(description = "Refund receipt number", 
    		example = "SMS-REC-20260723-000001")
    private String receiptNumber;

    @Schema(description = "Refund receipt generation date", 
    		example = "2026-07-23")
    private LocalDate receiptDate;

    @Schema(description = "Additional remarks related to refund", 
    		example = "Verified and approved successfully.")
    private String remarks;

    // Approval Information

    @Schema(description = "Approved by user ID", 
    		example = "12")
    private Long approvedByUserId;

    @Schema(description = "Approved by user name", 
    		example = "Admin User")
    private String approvedBy;

    @Schema(description = "Refund approval date and time", 
    		example = "2026-07-23T10:15:30")
    private LocalDateTime approvedDate;

    // Completion Information

    @Schema(description = "Completed by user ID", 
    		example = "15")
    private Long completedByUserId;

    @Schema(description = "Completed by user name", 
    		example = "Accounts Officer")
    private String completedBy;

    @Schema(description = "Refund completion date and time", 
    		example = "2026-07-24T15:45:10")
    private LocalDateTime completedDate;

    // Rejection Information

    @Schema(description = "Rejected by user ID", 
    		example = "8")
    private Long rejectedByUserId;

    @Schema(description = "Rejected by user name", 
    		example = "Finance Manager")
    private String rejectedBy;

    @Schema(description = "Reason for rejection", 
    		example = "Required documents are missing.")
    private String rejectionReason;

    @Schema(description = "Refund rejection date and time", 
    		example = "2026-07-23T11:30:45")
    private LocalDateTime rejectedDate;

    // Cancellation Information

    @Schema(description = "Cancelled by user ID", 
    		example = "20")
    private Long cancelledByUserId;

    @Schema(description = "Cancelled by user name", 
    		example = "System Administrator")
    private String cancelledBy;

    @Schema(description = "Reason for cancellation", 
    		example = "Refund request withdrawn by student.")
    private String cancellationReason;

    @Schema(description = "Refund cancellation date and time", 
    		example = "2026-07-23T16:20:15")
    private LocalDateTime cancelledDate;

    // Bank Refund Information

    @Schema(description = "Bank refund transaction reference", 
    		example = "TXN-REF-88990011")
    private String refundTransactionReference;

    @Schema(description = "Bank or payment gateway reference number", 
    		example = "BANKREF123456")
    private String bankReference;

    // Request Information

    @Schema(description = "Refund requested by user ID", 
    		example = "5")
    private Long requestedByUserId;

    @Schema(description = "Refund requested by user name", 
    		example = "Student Portal")
    private String requestedBy;

    @Schema(description = "Refund request date and time", 
    		example = "2026-07-22T09:45:30")
    private LocalDateTime requestedDate;
    
	 // =====================================================
	 // Delete Information
	 // =====================================================
	
	 @Schema(
	         description = "User ID who deleted the refund record",
	         example = "1"
	 )
	 private Long deletedByUserId;
	
	
	 @Schema(
	         description = "Name of the user who deleted the refund record",
	         example = "Admin User"
	 )
	 private String deletedBy;
	
	
	 @Schema(
	         description = "Date and time when the refund record was deleted",
	         example = "2026-07-25T10:30:00"
	 )
	 private LocalDateTime deletedDate;

}