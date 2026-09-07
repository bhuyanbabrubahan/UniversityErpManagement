package com.sms.fee.report.dailycollection.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Daily Collection Report Search Request")
public class DailyCollectionReportRequestDto {

	// =====================================================
	// Collection Date
	// =====================================================

	@Schema(description = "Collection Date From", example = "2026-07-01")
	private LocalDate collectionDateFrom;

	@Schema(description = "Collection Date To", example = "2026-07-31")
	private LocalDate collectionDateTo;

	// =====================================================
	// Student
	// =====================================================

	@Schema(description = "Student Id")
	private Long studentId;

	@Schema(description = "Student Name")
	private String studentName;

	@Schema(description = "Roll Number")
	private String rollNumber;

	// =====================================================
	// Admission
	// =====================================================

	@Schema(description = "Admission Id")
	private Long admissionId;

	@Schema(description = "Admission Number")
	private String admissionNumber;

	// =====================================================
	// Academic
	// =====================================================

	@Schema(description = "Academic Year")
	private String academicYear;

	@Schema(description = "Course Id")
	private Long courseId;

	@Schema(description = "Department Id")
	private Long departmentId;

	@Schema(description = "Semester Id")
	private Long semesterId;

	// =====================================================
	// Payment
	// =====================================================

	@Schema(description = "Payment Status")
	private PaymentStatus paymentStatus;

	@Schema(description = "Payment Mode")
	private PaymentMode paymentMode;

	// =====================================================
	// Amount
	// =====================================================

	@Schema(description = "Minimum Paid Amount")
	private BigDecimal minPaidAmount;

	@Schema(description = "Maximum Paid Amount")
	private BigDecimal maxPaidAmount;

	// =====================================================
	// Record Status
	// =====================================================

	@Schema(description = "Record Status")
	private RecordStatus status;

}
