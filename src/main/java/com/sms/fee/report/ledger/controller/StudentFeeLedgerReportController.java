package com.sms.fee.report.ledger.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportRequestDto;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportResponseDto;
import com.sms.fee.report.ledger.service.StudentFeeLedgerReportService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/ledger")
@Tag(
        name = "Student Fee Ledger Report",
        description = "Student Fee Ledger Report APIs"
)
public class StudentFeeLedgerReportController {


    private final StudentFeeLedgerReportService service;


    // =====================================================
    // Student Fee Ledger Report
    // =====================================================

    @Operation(
            summary = "Student Fee Ledger Report",
            description = "Fetch student fee ledger report with filters, pagination and sorting."
    )
    @GetMapping
    public ApiResponseDto<PageResponse<StudentFeeLedgerReportResponseDto>> getStudentFeeLedgerReport(

            @ModelAttribute
            StudentFeeLedgerReportRequestDto request,


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


        PageResponse<StudentFeeLedgerReportResponseDto> response =
                service.getStudentFeeLedgerReport(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Student fee ledger report fetched successfully",
                HttpStatus.OK
        ).getBody();

    }

}