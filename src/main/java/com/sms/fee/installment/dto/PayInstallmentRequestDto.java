package com.sms.fee.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.fee.enums.PaymentMode;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "PayInstallmentRequestDto",
        description = "Request DTO for paying a student fee installment."
)
public class PayInstallmentRequestDto {

    // =====================================================
    // Payment Amount
    // =====================================================

    @NotNull(
            message = "Paid amount is required."
    )
    @DecimalMin(
            value = "0.01",
            message = "Paid amount must be greater than zero."
    )
    @Schema(
            description = "Paid amount",
            example = "10000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal paidAmount;

    // =====================================================
    // Payment Date
    // =====================================================

    @NotNull(
            message = "Payment date is required."
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @Schema(
            description = "Payment date",
            example = "2026-08-10",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate paymentDate;

    // =====================================================
    // Payment Mode
    // =====================================================

    @NotNull(
            message = "Payment mode is required."
    )
    @Schema(
            description = "Payment mode",
            example = "ONLINE",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private PaymentMode paymentMode;

    // =====================================================
    // Transaction Reference
    // =====================================================

    @Schema(
            description = "Bank / UPI / Gateway transaction reference",
            example = "TXN987654321"
    )
    private String transactionReference;

    // =====================================================
    // Remarks
    // =====================================================

    @Schema(
            description = "Payment remarks",
            example = "First installment paid through UPI."
    )
    private String remarks;

}