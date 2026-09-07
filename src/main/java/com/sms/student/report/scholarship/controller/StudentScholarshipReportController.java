package com.sms.student.report.scholarship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.student.report.scholarship.dto.ScholarshipReportRequestDto;
import com.sms.student.report.scholarship.dto.ScholarshipReportResponseDto;
import com.sms.student.report.scholarship.service.StudentScholarshipReportService;
import com.sms.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student/reports/scholarships")
public class StudentScholarshipReportController {


    private final StudentScholarshipReportService reportService;


    // Scholarship Report

    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponse<ScholarshipReportResponseDto>>> getScholarshipReport(

            @ModelAttribute
            ScholarshipReportRequestDto request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction

    ) {


        PageResponse<ScholarshipReportResponseDto> response =
                reportService.getScholarshipReport(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Scholarship report fetched successfully",
                HttpStatus.OK
        );

    }

}