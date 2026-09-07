package com.sms.scholarship.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Student Scholarship Response DTO")
public class StudentScholarshipResponseDto {

    @Schema(
            description = "Scholarship ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Student ID",
            example = "101"
    )
    private Long studentId;

    @Schema(
            description = "Student Name",
            example = "Rahul Kumar"
    )
    private String studentName;

    @Schema(
            description = "Admission ID",
            example = "1"
    )
    private Long admissionId;

    @Schema(
            description = "Admission Number",
            example = "ADM20260001"
    )
    private String admissionNumber;

    @Schema(
            description = "Scholarship Name",
            example = "Merit Scholarship"
    )
    private String scholarshipName;

    @Schema(
            description = "Scholarship Type",
            example = "PERCENTAGE",
            allowableValues = {
                    "FIXED_AMOUNT",
                    "PERCENTAGE",
                    "FULL_FEE_WAIVER",
                    "PARTIAL_FEE_WAIVER"
            }
    )
    private ScholarshipType scholarshipType;

    @Schema(
            description = "Scholarship Value",
            example = "50.00"
    )
    private BigDecimal scholarshipValue;

    @Schema(
            description = "Requested Scholarship Amount",
            example = "25000.00"
    )
    private BigDecimal requestedAmount;

    @Schema(
            description = "Approved Scholarship Amount",
            example = "20000.00"
    )
    private BigDecimal approvedAmount;

    @Schema(
            description = "Academic Year",
            example = "2026-2027"
    )
    private String academicYear;


    @Schema(
            description = "Remarks",
            example = "Scholarship approved based on academic performance."
    )
    private String remarks;

    @Schema(
            description = "Reason for rejection, if applicable",
            example = "Required documents were not submitted."
    )
    private String rejectionReason;

    @Schema(
            description = "Scholarship Status",
            example = "APPROVED",
            allowableValues = {
                    "PENDING",
                    "APPROVED",
                    "REJECTED"
            }
    )
    private ScholarshipStatus scholarshipStatus;

    @Schema(
            description = "Record Status",
            example = "ACTIVE",
            allowableValues = {
                    "ACTIVE",
                    "INACTIVE",
                    "DELETED"
            }
    )
    private RecordStatus status;
    
	    
	 // =====================================================
	 // Creation Information
	 // =====================================================
	
	 @Schema(
	         description = "Created By User ID",
	         example = "101"
	 )
	 private Long createdByUserId;
	
	 @Schema(
	         description = "Created By",
	         example = "Admin"
	 )
	 private String createdBy;
	
	 @Schema(
	         description = "Created Date",
	         example = "2026-07-24T10:30:00"
	 )
	 private LocalDateTime createdDate;
	
	
	 // =====================================================
	 // Approval Information
	 // =====================================================
	
	 @Schema(
	         description = "Approved By User ID",
	         example = "102"
	 )
	 private Long approvedByUserId;
	
	 @Schema(
	         description = "Approved By",
	         example = "Principal"
	 )
	 private String approvedBy;
	
	 @Schema(
	         description = "Scholarship Approval Date",
	         example = "2026-07-20"
	 )
	 private LocalDateTime approvalDate;
	
	 // =====================================================
	 // Rejection Information
	 // =====================================================
	
	 @Schema(
	         description = "Rejected By User ID",
	         example = "103"
	 )
	 private Long rejectedByUserId;
	
	 @Schema(
	         description = "Rejected By",
	         example = "Scholarship Committee"
	 )
	 private String rejectedBy;
	
	 @Schema(
	         description = "Rejected Date",
	         example = "2026-07-24T12:00:00"
	 )
	 private LocalDateTime rejectedDate;
    
    
	// =====================================================
	// Delete Information
	// =====================================================

	@Schema(
	        description = "User ID who deleted the scholarship",
	        example = "1"
	)
	private Long deletedByUserId;

	@Schema(
	        description = "Name of the user who deleted the scholarship",
	        example = "Administrator"
	)
	private String deletedBy;

	@Schema(
	        description = "Date and time when the scholarship was deleted",
	        example = "2026-07-25T10:30:00"
	)
	private LocalDateTime deletedDate;

}