package com.sms.dashboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dashboard.service.DashboardService;
import com.sms.dto.DashboardResponseDto;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(
    name = "Dashboard Management",
    description = "Dashboard APIs for overall university statistics"
)
public class DashboardController {

    private final DashboardService service;

    @Operation(
        summary = "Get Dashboard Summary",
        description = "Returns overall dashboard statistics including students, admissions, attendance, exams and results."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard loaded successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<DashboardResponseDto>> getDashboard(
            Authentication authentication
    ){
        DashboardResponseDto response =
                service.getDashboard(authentication);


        return ResponseBuilder.success(
                response,
                "Dashboard loaded successfully",
                HttpStatus.OK
        );
    }
}
