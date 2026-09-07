package com.sms.fee.report.dto;


import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "MonthlyCollectionReportDto",
        description = "DTO containing month wise fee collection report details"
)
public class MonthlyCollectionReportDto {



    // =====================================================
    // Month Information
    // =====================================================


    @Schema(
            description = "Month number",
            example = "7"
    )
    private Integer month;



    @Schema(
            description = "Month name",
            example = "July"
    )
    private String monthName;



    @Schema(
            description = "Year of collection",
            example = "2026"
    )
    private Integer year;




    // =====================================================
    // Transaction Statistics
    // =====================================================


    @Schema(
            description = "Total number of fee transactions in month",
            example = "250"
    )
    private Long totalTransactions;




    // =====================================================
    // Collection Summary
    // =====================================================


    @Schema(
            description = "Total fee collected during month",
            example = "1500000.00"
    )
    private BigDecimal totalCollection;



}