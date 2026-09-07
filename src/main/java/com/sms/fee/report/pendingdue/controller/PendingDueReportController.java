package com.sms.fee.report.pendingdue.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.report.pendingdue.dto.PendingDueReportResponseDto;
import com.sms.fee.report.pendingdue.service.PendingDueReportService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-reports/pending-due")
@Tag(
        name = "Pending Due Reports",
        description = "APIs for pending fee due reports"
)
public class PendingDueReportController {



    private final PendingDueReportService service;



    // =====================================================
    // Pending Due Report
    // =====================================================


    @GetMapping
    @Operation(
            summary = "Get Pending Due Report",
            description =
            "Returns students having pending fee dues"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<PendingDueReportResponseDto>>> 
    getPendingDueReport(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,


            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ){


        return ResponseBuilder.success(

                service.getPendingDueReport(
                        page,
                        size
                ),

                "Pending due report generated successfully",

                HttpStatus.OK
        );

    }


}