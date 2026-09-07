package com.sms.fee.report.dto;

import java.math.BigDecimal;
import java.util.List;

import com.sms.fee.dto.StudentFeePaymentResponseDto;
import com.sms.fee.dto.StudentFeeReceiptResponseDto;
import com.sms.fee.enums.PaymentStatus;
import com.sms.fee.refund.dto.StudentFeeRefundResponseDto;
import com.sms.scholarship.dto.StudentScholarshipResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentPaymentHistoryResponseDto {

	// =====================================================
	// Student Information
	// =====================================================

	private Long studentId;

	private String studentName;

	private String rollNumber;

	private Long admissionId;

	private String admissionNumber;

	private String departmentName;

	private String courseName;

	// =====================================================
	// Financial Summary
	// =====================================================

	private BigDecimal totalFee;

	private BigDecimal totalPaidAmount;

	private BigDecimal totalScholarship;

	private BigDecimal totalRefundAmount;

	private BigDecimal totalDueAmount;
	
	private BigDecimal finalPayableAmount;

	private PaymentStatus paymentStatus;

	// =====================================================
	// History
	// =====================================================

	private List<StudentFeePaymentResponseDto> payments;

	private List<StudentFeeReceiptResponseDto> receipts;

	private List<StudentFeeRefundResponseDto> refunds;

	private List<StudentScholarshipResponseDto> scholarships;

}
