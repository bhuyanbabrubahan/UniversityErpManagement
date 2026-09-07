package com.sms.fee.refund.dto;


import java.math.BigDecimal;

import com.sms.fee.refund.enums.RefundMode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "StudentFeeRefundRequestDto",
        description = "Request DTO for creating student fee refund"
)
public class StudentFeeRefundRequestDto {



    // =====================================================
    // Payment Id
    // =====================================================


    @NotNull(
            message = "Payment id is required"
    )
    @Schema(
            description = "Fee payment id",
            example = "1"
    )
    private Long paymentId;





    // =====================================================
    // Refund Amount
    // =====================================================


    @NotNull(
            message = "Refund amount is required"
    )
    @DecimalMin(
            value = "0.01",
            message = "Refund amount must be greater than zero"
    )
    @Schema(
            description = "Refund amount",
            example = "20000.00"
    )
    private BigDecimal refundAmount;





    // =====================================================
    // Refund Reason
    // =====================================================


    @NotBlank(
            message = "Refund reason is required"
    )
    @Size(
            min = 5,
            max = 500,
            message = "Refund reason must be between 5 and 500 characters"
    )
    @Schema(
            description = "Reason for refund",
            example = "Admission cancelled"
    )
    private String refundReason;





    // =====================================================
    // Refund Mode
    // =====================================================


    @NotNull(
            message = "Refund mode is required"
    )
    @Schema(
            description = "Refund payment mode",
            example = "ONLINE"
    )
    private RefundMode refundMode;





    // =====================================================
    // Remarks
    // =====================================================


    @Schema(
            description = "Additional remarks",
            example = "Approved by accounts department"
    )
    @Size(
            max = 500,
            message = "Remarks cannot exceed 500 characters"
    )
    private String remarks;



}