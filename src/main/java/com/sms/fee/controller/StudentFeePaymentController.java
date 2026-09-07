package com.sms.fee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.StudentFeePaymentRequestDto;
import com.sms.fee.dto.StudentFeePaymentResponseDto;
import com.sms.fee.dto.StudentFeePaymentSearchRequest;
import com.sms.fee.service.StudentFeePaymentService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/student-fee-payments")
@RequiredArgsConstructor
@Tag(
        name = "Student Fee Payment",
        description = "APIs for managing student fee payments"
)
public class StudentFeePaymentController {


    private final StudentFeePaymentService service;



    // Create payment
    @PostMapping
    @Operation(
            summary = "Create student fee payment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeePaymentResponseDto>> createPayment(
            @Valid @RequestBody StudentFeePaymentRequestDto dto) {


        StudentFeePaymentResponseDto response =
                service.createPayment(dto);


        return ResponseBuilder.success(
                response,
                "Fee payment created successfully",
                HttpStatus.CREATED
        );
    }





    // Get payment by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Get fee payment by id"
    )
    public ResponseEntity<ApiResponseDto<StudentFeePaymentResponseDto>> getById(
            @PathVariable Long id) {


        StudentFeePaymentResponseDto response =
                service.getPaymentById(id);


        return ResponseBuilder.success(
                response,
                "Fee payment fetched successfully",
                HttpStatus.OK
        );
    }





    // Update payment
    @PutMapping("/{id}")
    @Operation(
            summary = "Update fee payment"
    )
    public ResponseEntity<ApiResponseDto<StudentFeePaymentResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentFeePaymentRequestDto dto) {


        StudentFeePaymentResponseDto response =
                service.updatePayment(id, dto);


        return ResponseBuilder.success(
                response,
                "Fee payment updated successfully",
                HttpStatus.OK
        );
    }





    // Soft delete payment
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete fee payment"
    )
    public ResponseEntity<ApiResponseDto<Void>> delete(
            @PathVariable Long id) {


        service.deletePayment(id);


        return ResponseBuilder.success(
                null,
                "Fee payment deleted successfully",
                HttpStatus.OK
        );
    }





    // Get all payments with pagination
    @GetMapping
    @Operation(
            summary = "Get all fee payments"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeePaymentResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {


        PageResponse<StudentFeePaymentResponseDto> response =
                service.getAllPayments(
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Fee payments fetched successfully",
                HttpStatus.OK
        );
    }





    // Search payments
    @PostMapping("/search")
    @Operation(
            summary = "Search fee payments"
    )
    public ResponseEntity<ApiResponseDto<PageResponse<StudentFeePaymentResponseDto>>> search(
            @Valid @RequestBody StudentFeePaymentSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {


        PageResponse<StudentFeePaymentResponseDto> response =
                service.searchPayments(
                        request,
                        page,
                        size,
                        sortBy,
                        direction
                );


        return ResponseBuilder.success(
                response,
                "Fee payment search completed",
                HttpStatus.OK
        );
    }

}