package com.sms.fee.report.dailycollection.controller;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.report.dailycollection.dto.DailyCollectionReportResponseDto;
import com.sms.fee.report.dailycollection.service.StudentDailyCollectionReportService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/daily-collection")
@Tag(
        name = "Daily Collection Report",
        description = "Daily Fee Collection Report APIs"
)
public class StudentDailyCollectionReportController {


    private final StudentDailyCollectionReportService reportService;


    // =====================================================
    // Daily Collection Report
    // =====================================================

    @Operation(
            summary = "Daily Collection Report",
            description = "Fetch daily fee collection report with date filter, pagination and sorting."
    )
    @GetMapping
    public ApiResponseDto<PageResponse<DailyCollectionReportResponseDto>> getDailyCollectionReport(

            @Parameter(description = "Payment Date (yyyy-MM-dd)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate paymentDate,


            @Parameter(description = "Page Number")
            @RequestParam(defaultValue = "0")
            int page,


            @Parameter(description = "Page Size")
            @RequestParam(defaultValue = "10")
            int size,


            @Parameter(description = "Sort Field")
            @RequestParam(defaultValue = "paymentDate")
            String sortBy,


            @Parameter(description = "Sort Direction (asc/desc)")
            @RequestParam(defaultValue = "desc")
            String direction

    ) {


        PageResponse<DailyCollectionReportResponseDto> response =
                reportService.getDailyCollectionReport(
                        paymentDate,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Daily collection report fetched successfully",
                HttpStatus.OK
        ).getBody();

    }

}