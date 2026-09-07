package com.sms.fee.installment.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Installment payment status"
)
public enum InstallmentStatus {

    // Installment Created

    @Schema(description = "Installment is pending")
    PENDING,

    // Partial Payment

    @Schema(description = "Installment partially paid")
    PARTIALLY_PAID,

    // Fully Paid

    @Schema(description = "Installment fully paid")
    PAID,

    // Due Date Crossed

    @Schema(description = "Installment overdue")
    OVERDUE,

    // Cancelled

    @Schema(description = "Installment cancelled")
    CANCELLED

}