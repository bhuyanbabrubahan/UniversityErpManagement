package com.sms.fee.service;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.StudentFeePaymentRequestDto;
import com.sms.fee.dto.StudentFeePaymentResponseDto;
import com.sms.fee.dto.StudentFeePaymentSearchRequest;

public interface StudentFeePaymentService {

	// Create Payment

	StudentFeePaymentResponseDto createPayment(StudentFeePaymentRequestDto dto);

	// Get By Id

	StudentFeePaymentResponseDto getPaymentById(Long id);

	// Update Payment

	StudentFeePaymentResponseDto updatePayment(Long id, StudentFeePaymentRequestDto dto);

	// Soft Delete

	void deletePayment(Long id);

	// Get All Pagination

	PageResponse<StudentFeePaymentResponseDto> getAllPayments(int page, int size, String sortBy, String direction);

	// Dynamic Search

	PageResponse<StudentFeePaymentResponseDto> searchPayments(StudentFeePaymentSearchRequest request, int page,
			int size, String sortBy, String direction);

}