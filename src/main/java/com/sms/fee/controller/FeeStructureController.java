package com.sms.fee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.FeeStructureRequestDto;
import com.sms.fee.dto.FeeStructureResponseDto;
import com.sms.fee.dto.FeeStructureSearchRequest;
import com.sms.fee.service.FeeStructureService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fee-structures")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Fee Structure Management",
        description = "APIs for managing fee structures."
)
public class FeeStructureController {

    private final FeeStructureService service;

    // ==========================================================
    // Create
    // ==========================================================

    @Operation(
            summary = "Create Fee Structure",
            description = "Creates a new fee structure."
    )
    @PostMapping
    public ResponseEntity<ApiResponseDto<FeeStructureResponseDto>> createFeeStructure(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Fee structure information",
                    required = true
            )
            @Valid
            @RequestBody
            FeeStructureRequestDto dto
    ) {

        FeeStructureResponseDto response =
                service.createFeeStructure(dto);

        return ResponseBuilder.success(
                response,
                "Fee structure created successfully.",
                HttpStatus.CREATED
        );

    }

    // ==========================================================
    // Get By Id
    // ==========================================================

    @Operation(
            summary = "Get Fee Structure By Id",
            description = "Returns fee structure details by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeeStructureResponseDto>> getFeeStructureById(

            @PathVariable
            Long id
    ) {

        FeeStructureResponseDto response =
                service.getFeeStructureById(id);

        return ResponseBuilder.success(
                response,
                "Fee structure fetched successfully.",
                HttpStatus.OK
        );

    }

    // ==========================================================
    // Get All
    // ==========================================================

    @Operation(
            summary = "Get All Fee Structures",
            description = "Returns paginated fee structures."
    )
    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponse<FeeStructureResponseDto>>> getAllFeeStructures(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        PageResponse<FeeStructureResponseDto> response =
                service.getAllFeeStructures(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Fee structures fetched successfully.",
                HttpStatus.OK
        );

    }

    // ==========================================================
    // Search
    // ==========================================================

    @Operation(
            summary = "Search Fee Structures",
            description = "Search fee structures using dynamic filters."
    )
    @PostMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponse<FeeStructureResponseDto>>> searchFeeStructures(

            @RequestBody
            FeeStructureSearchRequest request,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction

    ) {

        PageResponse<FeeStructureResponseDto> response =
                service.searchFeeStructures(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseBuilder.success(
                response,
                "Fee structures fetched successfully.",
                HttpStatus.OK
        );

    }

    // ==========================================================
    // Update
    // ==========================================================

    @Operation(
            summary = "Update Fee Structure",
            description = "Updates an existing fee structure."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<FeeStructureResponseDto>> updateFeeStructure(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            FeeStructureRequestDto dto

    ) {

        FeeStructureResponseDto response =
                service.updateFeeStructure(
                        id,
                        dto
                );

        return ResponseBuilder.success(
                response,
                "Fee structure updated successfully.",
                HttpStatus.OK
        );

    }

    // ==========================================================
    // Delete
    // ==========================================================

    @Operation(
            summary = "Delete Fee Structure",
            description = "Soft deletes a fee structure."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteFeeStructure(

            @PathVariable
            Long id

    ) {

        service.deleteFeeStructure(id);

        return ResponseBuilder.success(
                null,
                "Fee structure deleted successfully.",
                HttpStatus.OK
        );

    }

}