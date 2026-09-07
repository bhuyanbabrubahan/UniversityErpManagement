package com.sms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.dto.PageResponse;
import com.sms.dto.SemesterRequestDto;
import com.sms.dto.SemesterResponseDto;
import com.sms.dto.SemesterSearchRequest;
import com.sms.payload.ApiResponseDto;
import com.sms.service.SemesterService;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/semesters")
@Tag(
        name = "Semester Management",
        description = "APIs for managing semesters"
)
public class SemesterController {

    private final SemesterService service;

    public SemesterController(
            SemesterService service
    ) {
        this.service = service;
    }


    // =====================================================
    // CREATE
    // =====================================================

    @AuditLog(
            action = AuditAction.CREATE,
            module = "SEMESTER",
            description = "Create semester"
    )
    @PostMapping
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN')"
    )
    @Operation(
            summary = "Create semester",
            description = "Creates a new semester"
    )
    public ResponseEntity<ApiResponseDto<SemesterResponseDto>> createSemester(
            @Valid
            @RequestBody SemesterRequestDto dto
    ) {

        SemesterResponseDto response =
                service.createSemester(dto);

        return ResponseBuilder.success(
                response,
                "Semester created successfully",
                HttpStatus.CREATED
        );
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN','FACULTY','STUDENT')"
    )
    @Operation(
            summary = "Get semester by id"
    )
    public ResponseEntity<ApiResponseDto<SemesterResponseDto>> getById(
            @PathVariable Long id
    ) {

        SemesterResponseDto response =
                service.getSemesterById(id);

        return ResponseBuilder.success(
                response,
                "Semester fetched successfully",
                HttpStatus.OK
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN','FACULTY','STUDENT')"
    )
    @Operation(
            summary = "Get all semesters",
            description = "Fetch semesters with pagination and sorting"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<SemesterResponseDto>>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "semesterNumber")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        PageResponse<SemesterResponseDto> response =
                service.getAllSemesters(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Semesters fetched successfully",
                HttpStatus.OK
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @AuditLog(
            action = AuditAction.UPDATE,
            module = "SEMESTER",
            description = "Update semester"
    )
    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN')"
    )
    @Operation(
            summary = "Update semester",
            description = "Updates existing semester details"
    )
    public ResponseEntity<ApiResponseDto<SemesterResponseDto>> update(

            @PathVariable Long id,

            @Valid
            @RequestBody SemesterRequestDto dto

    ) {

        SemesterResponseDto response =
                service.updateSemester(
                        id,
                        dto
                );

        return ResponseBuilder.success(
                response,
                "Semester updated successfully",
                HttpStatus.OK
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    @AuditLog(
            action = AuditAction.DELETE,
            module = "SEMESTER",
            description = "Delete semester"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN')"
    )
    @Operation(
            summary = "Delete semester",
            description = "Deletes semester by id"
    )
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @PathVariable Long id
    ) {

        service.deleteSemester(id);

        return ResponseBuilder.success(
                null,
                "Semester deleted successfully",
                HttpStatus.OK
        );
    }


    // =====================================================
    // SEARCH
    // =====================================================

    @PostMapping("/search")
    @PreAuthorize(
            "hasAnyRole('SUPER_ADMIN','ADMIN','FACULTY')"
    )
    @Operation(
            summary = "Search semesters",
            description = "Search semesters dynamically"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<SemesterResponseDto>>> search(

            @RequestBody
            SemesterSearchRequest request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "semesterNumber")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        PageResponse<SemesterResponseDto> response =
                service.searchSemesters(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Semesters searched successfully",
                HttpStatus.OK
        );
    }
}
