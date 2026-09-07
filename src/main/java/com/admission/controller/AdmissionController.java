package com.admission.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.admission.dto.AdmissionRequestDto;
import com.admission.dto.AdmissionResponseDto;
import com.admission.dto.AdmissionSearchRequest;
import com.admission.service.AdmissionService;
import com.sms.dto.PageResponse;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
@Tag(
        name = "Admission Management",
        description = "APIs for managing student admissions"
)
public class AdmissionController {


    private final AdmissionService admissionService;



    // ---------------- CREATE ADMISSION ----------------
    @PostMapping
    @Operation(
            summary = "Create admission"
    )
    @AuditLog(
            action = AuditAction.CREATE,
            module = "ADMISSION",
            description = "Create admission"
    )
    public ResponseEntity<ApiResponseDto<AdmissionResponseDto>> createAdmission(
            @Valid @RequestBody AdmissionRequestDto dto) {


        AdmissionResponseDto response =
                admissionService.createAdmission(dto);


        return ResponseBuilder.success(
                response,
                "Admission created successfully",
                HttpStatus.CREATED
        );
    }



    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Get admission by id"
    )
    public ResponseEntity<ApiResponseDto<AdmissionResponseDto>> getAdmissionById(
            @PathVariable Long id) {


        AdmissionResponseDto response =
                admissionService.getAdmissionById(id);


        return ResponseBuilder.success(
                response,
                "Admission fetched successfully",
                HttpStatus.OK
        );
    }



    // ---------------- UPDATE ADMISSION ----------------
    @PutMapping("/{id}")
    @Operation(
            summary = "Update admission"
    )
    @AuditLog(
            action = AuditAction.UPDATE,
            module = "ADMISSION",
            description = "Update admission"
    )
    public ResponseEntity<ApiResponseDto<AdmissionResponseDto>> updateAdmission(
            @PathVariable Long id,
            @Valid @RequestBody AdmissionRequestDto dto) {


        AdmissionResponseDto response =
                admissionService.updateAdmission(
                        id,
                        dto
                );


        return ResponseBuilder.success(
                response,
                "Admission updated successfully",
                HttpStatus.OK
        );
    }



    // ---------------- CANCEL ADMISSION ----------------
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete admission"
    )
    @AuditLog(
            action = AuditAction.DELETE,
            module = "ADMISSION",
            description = "Delete admission"
    )
    public ResponseEntity<ApiResponseDto<Void>> deleteAdmission(
            @PathVariable Long id) {


        admissionService.deleteAdmission(id);


        return ResponseBuilder.success(
                null,
                "Admission cancelled successfully",
                HttpStatus.OK
        );
    }



    // ---------------- GET ALL WITH PAGINATION ----------------
    @GetMapping
    @Operation(
            summary = "Get all admissions with pagination"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<AdmissionResponseDto>>> getAllAdmissions(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction

    ) {


        PageResponse<AdmissionResponseDto> response =
                admissionService.getAllAdmissions(
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Admissions fetched successfully",
                HttpStatus.OK
        );
    }



    // ---------------- SEARCH ADMISSION ----------------
    @PostMapping("/search")
    @Operation(
            summary = "Search admissions dynamically"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<AdmissionResponseDto>>> searchAdmissions(

            @Valid @RequestBody AdmissionSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction

    ) {


        PageResponse<AdmissionResponseDto> response =
                admissionService.searchAdmissions(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Admissions searched successfully",
                HttpStatus.OK
        );
    }

}