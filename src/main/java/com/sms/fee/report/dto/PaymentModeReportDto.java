package com.sms.fee.report.dto;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "PaymentModeReportDto",
        description = "DTO containing payment mode wise fee collection report details"
)
public class PaymentModeReportDto {



    // =====================================================
    // Payment Mode Information
    // =====================================================


    @Schema(
            description = "Payment mode",
            example = "ONLINE"
    )
    private String paymentMode;



    // =====================================================
    // Transaction Statistics
    // =====================================================


    @Schema(
            description = "Total number of transactions using payment mode",
            example = "450"
    )
    private Long totalTransactions;



    // =====================================================
    // Collection Summary
    // =====================================================


    @Schema(
            description = "Total collected amount through payment mode",
            example = "2500000.00"
    )
    private BigDecimal totalCollection;



}