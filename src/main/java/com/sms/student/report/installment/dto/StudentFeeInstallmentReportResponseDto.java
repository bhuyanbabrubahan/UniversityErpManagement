package com.sms.student.report.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.fee.enums.PaymentMode;
import com.sms.fee.installment.enums.InstallmentStatus;

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
@Schema(description = "Installment Report Response")
public class StudentFeeInstallmentReportResponseDto {

    // =====================================================
    // Installment Information
    // =====================================================

    @Schema(description = "Installment ID", example = "1")
    private Long installmentId;

    @Schema(description = "Installment Number", example = "SMS-INS-20260725-ABC123")
    private String installmentNumber;

    @Schema(description = "Installment Sequence", example = "1")
    private Integer installmentSequence;

    @Schema(description = "Installment Status")
    private InstallmentStatus installmentStatus;

    // =====================================================
    // Student Information
    // =====================================================

    @Schema(description = "Student ID", example = "1")
    private Long studentId;

    @Schema(description = "Student Name", example = "Rahul Kumar Sharma")
    private String studentName;

    @Schema(description = "Roll Number", example = "STU20260001")
    private String rollNumber;

    // =====================================================
    // Admission Information
    // =====================================================

    @Schema(description = "Admission ID", example = "1")
    private Long admissionId;

    @Schema(description = "Admission Number", example = "ADM20260001")
    private String admissionNumber;

    // =====================================================
    // Academic Information
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
    // Amount Information
    // =====================================================

    @Schema(description = "Installment Amount", example = "15000.00")
    private BigDecimal installmentAmount;

    @Schema(description = "Paid Amount", example = "5000.00")
    private BigDecimal paidAmount;

    @Schema(description = "Remaining Amount", example = "10000.00")
    private BigDecimal remainingAmount;

    // =====================================================
    // Dates
    // =====================================================

    @Schema(description = "Due Date", example = "2026-08-15")
    private LocalDate dueDate;

	@Schema(description = "Fee payment identifier")
	private Long paymentId;
	
    @Schema(description = "Payment Date", example = "2026-08-10")
    private LocalDate paymentDate;

    // =====================================================
    // Payment Details
    // =====================================================

    @Schema(description = "Payment Mode")
    private PaymentMode paymentMode;

    @Schema(description = "Transaction Reference", example = "TXN987654321")
    private String transactionReference;

    @Schema(description = "Remarks", example = "First installment paid")
    private String remarks;

}
