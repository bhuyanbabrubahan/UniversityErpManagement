package com.sms.fee.dto;


import java.math.BigDecimal;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
    name = "StudentFeePaymentSearchRequest",
    description = "Request DTO used to search student fee payment records dynamically."
)
public class StudentFeePaymentSearchRequest {



    // =====================================================
    // Student Filter
    // =====================================================


    @Schema(
        description = "Search by Student ID",
        example = "1"
    )
    private Long studentId;



    // =====================================================
    // Admission Filter
    // =====================================================


    @Schema(
        description = "Search by Admission ID",
        example = "1"
    )
    private Long admissionId;




    // =====================================================
    // Academic Year
    // =====================================================


    @Schema(
        description = "Search by Academic Year",
        example = "2026-2027"
    )
    private String academicYear;




    // =====================================================
    // Payment Status
    // =====================================================


    @Schema(
        description = "Search by Payment Status",
        example = "PARTIAL",
        allowableValues = {
            "PAID",
            "PARTIAL",
            "PENDING"
        }
    )
    private PaymentStatus paymentStatus;




    // =====================================================
    // Payment Mode
    // =====================================================


    @Schema(
        description = "Search by Payment Mode",
        example = "UPI",
        allowableValues = {
            "CASH",
            "ONLINE",
            "CARD",
            "UPI"
        }
    )
    private PaymentMode paymentMode;




    // =====================================================
    // Paid Amount Range
    // =====================================================


    @Schema(
        description = "Minimum paid amount",
        example = "10000"
    )
    private BigDecimal minPaidAmount;



    @Schema(
        description = "Maximum paid amount",
        example = "50000"
    )
    private BigDecimal maxPaidAmount;




    // =====================================================
    // Record Status
    // =====================================================


    @Schema(
        description = "Search by record status",
        example = "ACTIVE",
        allowableValues = {
            "ACTIVE",
            "INACTIVE",
            "DELETED"
        }
    )
    private RecordStatus status;


}