package com.sms.fee.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.installment.enums.InstallmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "StudentFeeInstallmentResponseDto",
        description = "Response DTO containing installment details."
)
public class StudentFeeInstallmentResponseDto {

    // =====================================================
    // Primary Information
    // =====================================================

    @Schema(
            description = "Installment Id",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Installment Number",
            example = "SMS-INS-20260721-000001"
    )
    private String installmentNumber;

    @Schema(
            description = "Installment Sequence",
            example = "1"
    )
    private Integer installmentSequence;

    // =====================================================
    // Payment
    // =====================================================

    @Schema(
            description = "Fee Payment Id",
            example = "1"
    )
    private Long paymentId;

    // =====================================================
    // Student Snapshot
    // =====================================================

    @Schema(
            description = "Student Snapshot Id",
            example = "1"
    )
    private Long studentSnapshotId;

    @Schema(
            description = "Student Name",
            example = "Rahul Kumar Sharma"
    )
    private String studentName;

    @Schema(
            description = "Roll Number",
            example = "STU20260007"
    )
    private String rollNumber;

    // =====================================================
    // Admission Snapshot
    // =====================================================

    @Schema(
            description = "Admission Snapshot Id",
            example = "1"
    )
    private Long admissionSnapshotId;

    @Schema(
            description = "Admission Snapshot Number",
            example = "ADM20260001"
    )
    private String admissionSnapshotNumber;

    @Schema(
            description = "Academic Year",
            example = "2026-2027"
    )
    private String academicYear;

    // =====================================================
    // Amount
    // =====================================================

    @Schema(
            description = "Installment Amount",
            example = "25000.00"
    )
    private BigDecimal installmentAmount;

    @Schema(
            description = "Paid Amount",
            example = "10000.00"
    )
    private BigDecimal paidAmount;

    @Schema(
            description = "Remaining Amount",
            example = "15000.00"
    )
    private BigDecimal remainingAmount;

    // =====================================================
    // Dates
    // =====================================================

    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @Schema(
            description = "Due Date",
            example = "2026-08-15"
    )
    private LocalDate dueDate;

    @JsonFormat(
            pattern = "yyyy-MM-dd"
    )
    @Schema(
            description = "Payment Date",
            example = "2026-08-10"
    )
    private LocalDate paymentDate;

    // =====================================================
    // Payment Details
    // =====================================================

    @Schema(
            description = "Payment Mode",
            example = "ONLINE"
    )
    private PaymentMode paymentMode;

    @Schema(
            description = "Transaction Reference",
            example = "TXN987654321"
    )
    private String transactionReference;

    // =====================================================
    // Status
    // =====================================================

    @Schema(
            description = "Installment Status",
            example = "PENDING"
    )
    private InstallmentStatus installmentStatus;

    // =====================================================
    // Remarks
    // =====================================================

    @Schema(
            description = "Remarks",
            example = "First installment"
    )
    private String remarks;
    
	 // =====================================================
	 // Creation Audit Information
	 // =====================================================
	
	 @Schema(
	         description = "User id who created the installment",
	         example = "101"
	 )
	 private Long createdByUserId;
	
	
	 @Schema(
	         description = "Name of user who created the installment",
	         example = "Admin"
	 )
	 private String createdBy;
	
	
	 @Schema(
	         description = "Installment creation date and time",
	         example = "2026-07-24T10:30:00"
	 )
	 private LocalDateTime createdDate;
	
	
	
	 // =====================================================
	 // Update Audit Information
	 // =====================================================
	
	 @Schema(
	         description = "User id who last updated the installment",
	         example = "102"
	 )
	 private Long updatedByUserId;
	
	
	 @Schema(
	         description = "Name of user who last updated the installment",
	         example = "Admin"
	 )
	 private String updatedBy;
	
	
	 @Schema(
	         description = "Last update date and time",
	         example = "2026-07-24T11:30:00"
	 )
	 private LocalDateTime updatedDate;
	 
	 
	 
	// =====================================================
	// Completion Information
	// =====================================================

	@Schema(
	        description = "User id who completed the installment",
	        example = "101"
	)
	private Long completedByUserId;


	@Schema(
	        description = "Name of user who completed the installment",
	        example = "Admin"
	)
	private String completedBy;


	@Schema(
	        description = "Completion date and time",
	        example = "2026-07-24T12:30:00"
	)
	private LocalDateTime completedDate;
	 
	@Schema(
	        description = "User id who deleted record",
	        example = "1"
	)
	private Long deletedByUserId;


	@Schema(
	        description = "User name who deleted record",
	        example = "Admin User"
	)
	private String deletedBy;


	@Schema(
	        description = "Record deletion date time",
	        example = "2026-07-25 10:30:00"
	)
	private LocalDateTime deletedDate;

}