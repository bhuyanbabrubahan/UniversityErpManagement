package com.sms.fee.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// =====================================================
// Fee Collection Report Response DTO
// =====================================================

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Fee Collection Report Response")
public class FeeCollectionReportResponseDto {

    @Schema(
            description = "Receipt Number",
            example = "REC000001"
    )
    private String receiptNumber;

    @Schema(
            description = "Receipt Id"
    )
    private Long receiptId;

    @Schema(
            description = "Student ID",
            example = "10"
    )
    private Long studentId;

    @Schema(
            description = "Student Name",
            example = "Rahul Sharma"
    )
    private String studentName;

    @Schema(
            description = "Roll Number",
            example = "CSE2026001"
    )
    private String rollNumber;

    @Schema(
            description = "Admission Number",
            example = "ADM2026001"
    )
    private String admissionNumber;

    @Schema(
            description = "Department Name",
            example = "Computer Science"
    )
    private String departmentName;

    @Schema(
            description = "Course Name",
            example = "B.Tech"
    )
    private String courseName;


    @Schema(
            description = "Academic Year",
            example = "2026-2027"
    )
    private String academicYear;

    @Schema(
            description = "Payment Date",
            example = "2026-07-22"
    )
    private LocalDate paymentDate;

    @Schema(
            description = "Receipt Date",
            example = "2026-07-22"
    )
    private LocalDate receiptDate;

    @Schema(description = "Payment Mode")
    private PaymentMode paymentMode;

    @Schema(description = "Payment Status")
    private PaymentStatus paymentStatus;

    @Schema(
            description = "Transaction Reference",
            example = "TXN987654321"
    )
    private String transactionReference;

    @Schema(
            description = "Total Fee",
            example = "58000.00"
    )
    private BigDecimal totalFee;

    @Schema(
            description = "Scholarship Amount",
            example = "14500.00"
    )
    private BigDecimal scholarshipAmount;
    
    @Schema(
    	    description = "Scholarship name",
    	    example = "State Government Scholarship"
    )
    	private String scholarshipName;

    @Schema(
    	    description = "Type of scholarship",
    	    example = "FIXED_AMOUNT",
    	allowableValues = {
    	        "FIXED_AMOUNT",
    	        "PERCENTAGE"
    	}
    )
    	private ScholarshipType scholarshipType;

    @Schema(
            description = "Final Payable Amount",
            example = "43500.00"
    )
    private BigDecimal finalPayableAmount;

    @Schema(
            description = "Paid Amount",
            example = "18800.00"
    )
    private BigDecimal paidAmount;

    @Schema(
            description = "Due Amount",
            example = "24700.00"
    )
    private BigDecimal dueAmount;

    @Schema(
    	    description = "User who created the record",
    	    example = "admin"
    	)
   	private String createdBy;

    	@Schema(
    	    description = "Date and time when the record was created",
    	    example = "2026-07-25T10:30:00"
    	)
    private LocalDateTime createdDate;

}