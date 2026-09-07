package com.sms.fee.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "StudentFeeInstallmentRequestDto",
        description = "Request DTO for creating a student fee installment."
)
public class StudentFeeInstallmentRequestDto {

    // =====================================================
    // Payment
    // =====================================================

    @NotNull(
            message = "Payment Id is required."
    )
    @Positive(
            message = "Payment Id must be greater than zero."
    )
    @Schema(
            description = "Student fee payment id",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long paymentId;

    // =====================================================
    // Installment
    // =====================================================

    @NotNull(
            message = "Installment sequence is required."
    )
    @Positive(
            message = "Installment sequence must be greater than zero."
    )
    @Schema(
            description = "Installment sequence number",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer installmentSequence;

    @NotNull(
            message = "Installment amount is required."
    )
    @DecimalMin(
            value = "0.01",
            message = "Installment amount must be greater than zero."
    )
    @Schema(
            description = "Installment amount",
            example = "25000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal installmentAmount;

    // =====================================================
    // Due Date
    // =====================================================

    @NotNull(
            message = "Due date is required."
    )
    @FutureOrPresent(
            message = "Due date cannot be in the past."
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @Schema(
            description = "Installment due date",
            example = "2026-08-15",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate dueDate;

    // =====================================================
    // Remarks
    // =====================================================

    @Schema(
            description = "Additional remarks",
            example = "First installment"
    )
    private String remarks;

}