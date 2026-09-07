package com.sms.fee.installment.service;

import com.sms.dto.PageResponse;
import com.sms.fee.installment.dto.PayInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentRequestDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentResponseDto;
import com.sms.fee.installment.dto.StudentFeeInstallmentSearchRequestDto;

public interface StudentFeeInstallmentService {

    // Create

    StudentFeeInstallmentResponseDto createInstallment(
            StudentFeeInstallmentRequestDto request
    );

    // Update

    StudentFeeInstallmentResponseDto updateInstallment(
            Long id,
            StudentFeeInstallmentRequestDto request
    );

    // Pay Installment

    StudentFeeInstallmentResponseDto payInstallment(
            Long id,
            PayInstallmentRequestDto request
    );

    // Get By Id

    StudentFeeInstallmentResponseDto getInstallmentById(
            Long id
    );

    // Get All

    PageResponse<StudentFeeInstallmentResponseDto> getAllInstallments(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    // Search

    PageResponse<StudentFeeInstallmentResponseDto> searchInstallments(
            StudentFeeInstallmentSearchRequestDto request
    );

    // Soft Delete

    void deleteInstallment(
            Long id
    );

}