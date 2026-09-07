package com.sms.fee.installment.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.dto.PageResponse;
import com.sms.fee.installment.dto.PayInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentResponseDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentSearchRequestDto;
import com.sms.fee.installment.service.StudentFeeInstallmentService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/fee-installments")
@RequiredArgsConstructor
@Tag(
        name = "Student Fee Installment Management",
        description = "APIs for creating, updating, paying and managing student fee installments"
)
public class StudentFeeInstallmentController {

    private final StudentFeeInstallmentService installmentService;

    // =====================================================
    // Create Installment
    // =====================================================

    @PostMapping
    @Operation(
            summary = "Create installment",
            description = "Create a new student fee installment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeInstallmentResponseDto>> create(

            @Valid
            @RequestBody
            StudentFeeInstallmentRequestDto request

    ) {

        return ResponseBuilder.success(

                installmentService.createInstallment(request),

                "Installment created successfully",

                HttpStatus.CREATED
        );
    }
    
    
    

    // =====================================================
    // Update Installment
    // =====================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update installment",
            description = "Update pending or partially paid installment details"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeInstallmentResponseDto>> update(

            @Parameter(
                    description = "Installment ID",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            StudentFeeInstallmentRequestDto request

    ) {

        return ResponseBuilder.success(

                installmentService.updateInstallment(
                        id,
                        request
                ),

                "Installment updated successfully",

                HttpStatus.OK
        );
    }
    
    
    

    // =====================================================
    // Pay Installment
    // =====================================================

    @PatchMapping("/{id}/pay")
    @Operation(
            summary = "Pay installment",
            description = "Make payment against a student fee installment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeInstallmentResponseDto>> pay(

            @Parameter(
                    description = "Installment ID",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            PayInstallmentRequestDto request

    ) {

        return ResponseBuilder.success(

                installmentService.payInstallment(
                        id,
                        request
                ),

                "Installment payment completed successfully",

                HttpStatus.OK
        );
    }

    
    
    
    
    // =====================================================
    // Get Installment By Id
    // =====================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get installment by id",
            description = "Fetch installment details using installment id"
    )
    public ResponseEntity<ApiResponseDto<StudentFeeInstallmentResponseDto>> getById(

            @Parameter(
                    description = "Installment ID",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id

    ) {

        return ResponseBuilder.success(

                installmentService.getInstallmentById(id),

                "Installment fetched successfully",

                HttpStatus.OK
        );
    }



    
    // =====================================================
    // Get All Installments
    // =====================================================

    @GetMapping
    @Operation(
            summary = "Get all installments",
            description = "Fetch all student fee installments with pagination and sorting"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeInstallmentResponseDto>>> getAll(

            @Parameter(
                    description = "Page number",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(
                    description = "Page size",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            int size,

            @Parameter(
                    description = "Sort field",
                    example = "id"
            )
            @RequestParam(defaultValue = "id")
            String sortBy,

            @Parameter(
                    description = "Sort direction (asc/desc)",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String sortDirection

    ) {

        return ResponseBuilder.success(

                installmentService.getAllInstallments(
                        page,
                        size,
                        sortBy,
                        sortDirection
                ),

                "Installments fetched successfully",

                HttpStatus.OK
        );
    }



    // =====================================================
    // Search Installments
    // =====================================================

    @PostMapping("/search")
    @Operation(
            summary = "Search installments",
            description = "Search student fee installments using dynamic filters with pagination"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeeInstallmentResponseDto>>> search(

            @Valid
            @RequestBody
            StudentFeeInstallmentSearchRequestDto request

    ) {

        return ResponseBuilder.success(

                installmentService.searchInstallments(request),

                "Installment search completed successfully",

                HttpStatus.OK
        );
    }



    // =====================================================
    // Delete Installment
    // =====================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete installment",
            description = "Soft delete a student fee installment"
    )
    public ResponseEntity<ApiResponseDto<Void>> delete(

            @Parameter(
                    description = "Installment ID",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id

    ) {

        installmentService.deleteInstallment(id);

        return ResponseBuilder.success(

                null,

                "Installment deleted successfully",

                HttpStatus.OK
        );
    }




}