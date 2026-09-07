package com.sms.fee.service;

import com.sms.dto.PageResponse;
import com.sms.fee.dto.StudentFeeReceiptRequestDto;
import com.sms.fee.dto.StudentFeeReceiptResponseDto;
import com.sms.fee.dto.StudentFeeReceiptSearchRequest;

public interface StudentFeeReceiptService {

    // Generate Receipt

	StudentFeeReceiptResponseDto generateReceipt(
	        Long paymentId,
	        StudentFeeReceiptRequestDto dto);

    // Get By Id

    StudentFeeReceiptResponseDto getReceiptById(
            Long id
    );

    
    // Soft Delete

    void deleteReceipt(
            Long id
    );

    // Get All

    PageResponse<StudentFeeReceiptResponseDto> getAllReceipts(
            int page,
            int size,
            String sortBy,
            String direction
    );

    // Search

    PageResponse<StudentFeeReceiptResponseDto> searchReceipts(
            StudentFeeReceiptSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );
    
    


    // =====================================================
    // Print / Download Receipt PDF
    // =====================================================

    byte[] printReceipt(
            Long id
    );

}