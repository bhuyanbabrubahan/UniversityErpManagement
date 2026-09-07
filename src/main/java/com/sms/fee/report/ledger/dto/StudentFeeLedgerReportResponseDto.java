package com.sms.fee.report.ledger.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Student Fee Ledger Report Response")
public class StudentFeeLedgerReportResponseDto {

	// =====================================================
	// Payment Information
	// =====================================================

	@Schema(description = "Payment ID", example = "1")
	private Long paymentId;

	@Schema(description = "Academic Year", example = "2026-2027")
	private String academicYear;

	// =====================================================
	// Student Information
	// =====================================================

	@Schema(description = "Student ID", example = "1")
	private Long studentId;

	@Schema(description = "Student Name", example = "Rahul Kumar Sharma")
	private String studentName;

	@Schema(description = "Roll Number", example = "STU20260007")
	private String rollNumber;

	// =====================================================
	// Admission Information
	// =====================================================

	@Schema(description = "Admission ID", example = "1")
	private Long admissionId;

	@Schema(description = "Admission Number", example = "ADM20260001")
	private String admissionNumber;

	@Schema(description = "Course Name", example = "Information Technology")
	private String courseName;

	@Schema(description = "Department Name", example = "Information Technology")
	private String departmentName;

	@Schema(description = "Semester Name", example = "Semester I")
	private String semesterName;

	// =====================================================
	// Fee Summary
	// =====================================================

	@Schema(description = "Total Fee", example = "50000")
	private BigDecimal totalFee;

	@Schema(description = "Final Payable Amount", example = "45000")
	private BigDecimal finalPayableAmount;

	@Schema(description = "Paid Amount", example = "30000")
	private BigDecimal paidAmount;

	@Schema(description = "Due Amount", example = "15000")
	private BigDecimal dueAmount;

	// =====================================================
	// Payment Information
	// =====================================================

	@Schema(description = "Payment Date", example = "2026-07-25")
	private LocalDate paymentDate;

	@Schema(description = "Payment Mode")
	private PaymentMode paymentMode;

	@Schema(description = "Payment Status")
	private PaymentStatus paymentStatus;

	@Schema(description = "Transaction Reference", example = "TXN202607250001")
	private String transactionReference;

}