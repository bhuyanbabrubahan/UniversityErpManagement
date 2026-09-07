package com.sms.report.refund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.report.refund.dto.RefundReportRequestDto;
import com.sms.report.refund.dto.RefundReportResponseDto;
import com.sms.report.refund.service.StudentFeeRefundReportService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fee-reports/refunds")
@RequiredArgsConstructor
@Tag(
        name = "Refund Report",
        description = "Advanced Fee Refund Report APIs"
)
public class StudentFeeRefundReportController {

    private final StudentFeeRefundReportService refundReportService;

    @Operation(
            summary = "Search Refund Report",
            description = "Search fee refund report with filters, pagination and sorting."
    )
    @PostMapping("/search")
    public ApiResponseDto<PageResponse<RefundReportResponseDto>> searchRefundReport(

            @RequestBody RefundReportRequestDto request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

    	PageResponse<RefundReportResponseDto> response =
    	        refundReportService.getRefundReport(
    	                request,
    	                page,
    	                size,
    	                sortBy,
    	                direction
    	        );

    	return ResponseBuilder.success(
    	        response,
    	        "Refund report fetched successfully",
    	        HttpStatus.OK
    	).getBody();
    }

}