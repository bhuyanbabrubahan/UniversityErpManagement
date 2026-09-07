package com.sms.student.report.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.installment.enums.InstallmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Installment Report Search Request")
public class StudentFeeInstallmentReportRequestDto {

	// Student Filters

	@Schema(description = "Student ID", example = "1")
	private Long studentId;

	@Schema(description = "Student Name", example = "Rahul Kumar Sharma")
	private String studentName;

	@Schema(description = "Roll Number", example = "STU20260001")
	private String rollNumber;

	// Admission Filters

	@Schema(description = "Admission ID", example = "1")
	private Long admissionId;

	@Schema(description = "Admission Number", example = "ADM20260001")
	private String admissionNumber;

	// Academic Filters

	@Schema(description = "Academic Year", example = "2026-2027")
	private String academicYear;

	@Schema(description = "Course Name", example = "Information Technology")
	private String courseName;

	@Schema(description = "Department Name", example = "Information Technology")
	private String departmentName;

	@Schema(description = "Semester Name", example = "Semester I")
	private String semesterName;

	// Installment Filters

	@Schema(description = "Installment Number", example = "SMS-INS-20260725-ABC123")
	private String installmentNumber;

	@Schema(description = "Installment Sequence", example = "1")
	private Integer installmentSequence;

	@Schema(description = "Installment Status")
	private InstallmentStatus installmentStatus;

	// Amount Filters

	@Schema(description = "Minimum Installment Amount", example = "5000")
	private BigDecimal minInstallmentAmount;

	@Schema(description = "Maximum Installment Amount", example = "20000")
	private BigDecimal maxInstallmentAmount;

	@Schema(description = "Minimum Paid Amount", example = "1000")
	private BigDecimal minPaidAmount;

	@Schema(description = "Maximum Paid Amount", example = "15000")
	private BigDecimal maxPaidAmount;

	@Schema(description = "Minimum Remaining Amount", example = "0")
	private BigDecimal minRemainingAmount;

	@Schema(description = "Maximum Remaining Amount", example = "10000")
	private BigDecimal maxRemainingAmount;

	// Due Date Filters

	@Schema(description = "Due Date From", example = "2026-07-01")
	private LocalDate dueDateFrom;

	@Schema(description = "Due Date To", example = "2026-07-31")
	private LocalDate dueDateTo;

	// Payment Date Filters
	
	@Schema(description = "Fee payment identifier")
	private Long paymentId;

	@Schema(description = "Payment mode used for installment payment")
	private PaymentMode paymentMode;

	@Schema(description = "Payment Date From", example = "2026-07-01")
	private LocalDate paymentDateFrom;

	@Schema(description = "Payment Date To", example = "2026-07-31")
	private LocalDate paymentDateTo;

	// Record Status

	@Schema(description = "Record Status")
	private RecordStatus status;

}