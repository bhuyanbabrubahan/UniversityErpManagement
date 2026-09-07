package com.sms.fee.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.fee.enums.PaymentMode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
    name = "StudentFeePaymentRequestDto",
    description = "Request DTO used to create or update student fee payment."
)
public class StudentFeePaymentRequestDto {


    // ==========================
    // Student
    // ==========================

    @Schema(
        description = "Student ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
        message = "Student id is required"
    )
    private Long studentId;


    @Schema(
    		 description = "Scholarship id if applicable",
    		 example = "1"
    		)
    private Long scholarshipId;
    
    
    // ==========================
    // Admission
    // ==========================

    @Schema(
        description = "Admission ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
        message = "Admission id is required"
    )
    private Long admissionId;



    // ==========================
    // Academic Year
    // ==========================

    @Schema(
        description = "Academic year",
        example = "2026-2027",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
        message = "Academic year is required"
    )
    private String academicYear;



    // ==========================
    // Payment
    // ==========================

    @Schema(
        description = "Paid amount by student",
        example = "30000",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
        message = "Paid amount is required"
    )
    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "Paid amount cannot be negative"
    )
    private BigDecimal paidAmount;



    @Schema(
        description = "Payment date",
        example = "2026-07-20"
    )
    private LocalDate paymentDate;



    @Schema(
        description = "Payment mode",
        example = "ONLINE",
        allowableValues = {
            "CASH",
            "ONLINE",
            "CARD",
            "UPI"
        }
    )
    private PaymentMode paymentMode;



    @Schema(
        description = "Transaction reference number",
        example = "TXN123456789"
    )
    @Size(
        max = 100,
        message = "Transaction reference cannot exceed 100 characters"
    )
    private String transactionReference;



    @Schema(
        description = "Additional remarks",
        example = "First semester fee payment"
    )
    @Size(
        max = 500,
        message = "Remarks cannot exceed 500 characters"
    )
    private String remarks;

}