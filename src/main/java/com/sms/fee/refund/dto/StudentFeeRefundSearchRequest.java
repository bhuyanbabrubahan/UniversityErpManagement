package com.sms.fee.refund.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.fee.refund.enums.RefundMode;
import com.sms.fee.refund.enums.RefundStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "StudentFeeRefundSearchRequest",
        description = "Search request DTO for filtering student fee refunds"
)
public class StudentFeeRefundSearchRequest {



    // =====================================================
    // Refund Number
    // =====================================================


    @Schema(
            description = "Refund number",
            example = "SMS-REF-20260721-000001"
    )
    private String refundNumber;





    // =====================================================
    // Student Search
    // =====================================================


    @Schema(
            description = "Student name",
            example = "Rahul Kumar"
    )
    private String studentName;




    @Schema(
            description = "Student id",
            example = "1"
    )
    private Long studentId;





    // =====================================================
    // Payment Reference
    // =====================================================


    @Schema(
            description = "Payment id",
            example = "1"
    )
    private Long paymentId;





    // =====================================================
    // Refund Status
    // =====================================================


    @Schema(
            description = "Refund status",
            example = "APPROVED"
    )
    private RefundStatus refundStatus;





    // =====================================================
    // Refund Mode
    // =====================================================


    @Schema(
            description = "Refund mode",
            example = "ONLINE"
    )
    private RefundMode refundMode;





    // =====================================================
    // Date Filter
    // =====================================================


    @Schema(
            description = "Refund start date",
            example = "2026-07-01"
    )
    private LocalDate fromDate;




    @Schema(
            description = "Refund end date",
            example = "2026-07-31"
    )
    private LocalDate toDate;
    
    
    @Schema(
            description = "Student roll number",
            example = "BCA2026001"
    )
    private String rollNumber;
    
    
    @Schema(
            description = "Admission number",
            example = "ADM20260001"
    )
    private String admissionNumber;
    
    
    private BigDecimal minRefundAmount;

    private BigDecimal maxRefundAmount;
    
    private String approvedBy;
    
    private String completedBy;
    
    private String rejectedBy;



}