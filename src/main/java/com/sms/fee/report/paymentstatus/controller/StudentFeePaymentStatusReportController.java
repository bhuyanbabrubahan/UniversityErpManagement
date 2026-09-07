package com.sms.fee.report.paymentstatus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportRequestDto;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportResponseDto;
import com.sms.fee.report.paymentstatus.service.StudentFeePaymentStatusReportService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/payment-status")
@Tag(
        name = "Payment Status Report",
        description = "Student Fee Payment Status Report APIs"
)
public class StudentFeePaymentStatusReportController {


    private final StudentFeePaymentStatusReportService reportService;


    // =====================================================
    // Payment Status Report
    // =====================================================

    @Operation(
            summary = "Payment Status Report",
            description = "Fetch payment status report with filters, pagination and sorting."
    )
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponse<PaymentStatusReportResponseDto>>> searchPaymentStatusReport(

            @RequestBody
            PaymentStatusReportRequestDto request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "paymentDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction

    ) {


        PageResponse<PaymentStatusReportResponseDto> response =
                reportService.searchPaymentStatusReport(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Payment status report fetched successfully",
                HttpStatus.OK
        );

    }

}