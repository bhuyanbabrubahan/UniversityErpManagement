package com.sms.fee.report.ledger.dto;


	import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "Student Fee Ledger Report Search Request")
	public class StudentFeeLedgerReportRequestDto {

		// =====================================================
		// Student Filters
		// =====================================================

		@Schema(description = "Student ID", example = "1")
		private Long studentId;

		@Schema(description = "Student Name", example = "Rahul Kumar Sharma")
		private String studentName;

		@Schema(description = "Roll Number", example = "STU20260007")
		private String rollNumber;

		// =====================================================
		// Admission Filters
		// =====================================================

		@Schema(description = "Admission ID", example = "1")
		private Long admissionId;

		@Schema(description = "Admission Number", example = "ADM20260001")
		private String admissionNumber;

		// =====================================================
		// Academic Filters
		// =====================================================

		@Schema(description = "Academic Year", example = "2026-2027")
		private String academicYear;

		@Schema(description = "Course Name", example = "Information Technology")
		private String courseName;

		@Schema(description = "Department Name", example = "Information Technology")
		private String departmentName;

		@Schema(description = "Semester Name", example = "Semester I")
		private String semesterName;

		// =====================================================
		// Payment Filters
		// =====================================================

		@Schema(description = "Payment Status")
		private PaymentStatus paymentStatus;

		@Schema(description = "Payment Date From")
		private LocalDate paymentDateFrom;

		@Schema(description = "Payment Date To")
		private LocalDate paymentDateTo;

		// =====================================================
		// Record Status
		// =====================================================

		@Schema(description = "Record Status")
		private RecordStatus status;

	}