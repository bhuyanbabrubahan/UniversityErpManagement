package com.sms.student.report.installment.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportRequestDto;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportResponseDto;
import com.sms.student.report.installment.service.StudentFeeInstallmentReportService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/installments")
@Tag(name = "Installment Report", description = "Student Fee Installment Report APIs")
public class StudentFeeInstallmentReportController {

	private final StudentFeeInstallmentReportService reportService;


	// =====================================================
	// Installment Report
	// =====================================================

	@Operation(
			summary = "Installment Report",
			description = "Fetch installment report with filters, pagination and sorting."
	)
	@GetMapping
	public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeInstallmentReportResponseDto>>> getInstallmentReport(

	        @ModelAttribute StudentFeeInstallmentReportRequestDto request,

	        @Parameter(description = "Page Number")
	        @RequestParam(defaultValue = "0")
	        int page,

	        @Parameter(description = "Page Size")
	        @RequestParam(defaultValue = "10")
	        int size,

	        @Parameter(description = "Sort Field")
	        @RequestParam(defaultValue = "id")
	        String sortBy,

	        @Parameter(description = "Sort Direction (asc/desc)")
	        @RequestParam(defaultValue = "desc")
	        String direction) {


	    PageResponse<StudentFeeInstallmentReportResponseDto> response =
	            reportService.getInstallmentReport(
	                    request,
	                    page,
	                    size,
	                    sortBy,
	                    direction
	            );


	    return ResponseBuilder.success(
	            response,
	            "Installment report fetched successfully",
	            HttpStatus.OK
	    );

	}

}
