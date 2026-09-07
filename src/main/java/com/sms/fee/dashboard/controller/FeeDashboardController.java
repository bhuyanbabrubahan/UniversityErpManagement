package com.sms.fee.dashboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.fee.dashboard.dto.FeeDashboardResponseDto;
import com.sms.fee.dashboard.service.FeeDashboardService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fee-dashboard")
@Validated
@Tag(
        name = "Fee Dashboard",
        description = "Finance dashboard APIs for fee collection, dues, payment statistics and scholarship summary"
)
public class FeeDashboardController {

    private final FeeDashboardService feeDashboardService;

    // =====================================================
    // Fee Dashboard
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get Fee Dashboard",
            description = "Returns complete finance dashboard including fee collection, payment statistics, pending dues, payment mode summary and scholarship summary"
    )
    public ResponseEntity<ApiResponseDto<FeeDashboardResponseDto>> getDashboard() {

        return ResponseBuilder.success(

                feeDashboardService.getDashboard(),

                "Fee dashboard loaded successfully",

                HttpStatus.OK
        );
    }
    
   

}