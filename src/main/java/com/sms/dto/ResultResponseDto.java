package com.sms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;
import com.sms.enums.ResultStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "ResultResponseDto",
    description = "Response DTO containing complete examination result details."
)
public class ResultResponseDto {

    // ==========================
    // Primary Key
    // ==========================

    @Schema(
            description = "Unique Result ID",
            example = "10"
    )
    private Long id;

    // ==========================
    // Student Details
    // ==========================

    @Schema(
            description = "Student ID",
            example = "1"
    )
    private Long studentId;

    @Schema(
            description = "Student Roll Number",
            example = "STU20260001"
    )
    private String rollNumber;

    @Schema(
            description = "Full Name of Student",
            example = "Rahul Sharma"
    )
    private String studentName;

    // ==========================
    // Exam Details
    // ==========================

    @Schema(
            description = "Exam ID",
            example = "5"
    )
    private Long examId;

    @Schema(
            description = "Exam Name",
            example = "Mid Semester Examination"
    )
    private String examName;

    // ==========================
    // Subject Details
    // ==========================

    @Schema(
            description = "Subject ID",
            example = "3"
    )
    private Long subjectId;

    @Schema(
            description = "Subject Name",
            example = "Object Oriented Programming"
    )
    private String subjectName;

    // ==========================
    // Semester Details
    // ==========================

    @Schema(
            description = "Semester ID",
            example = "2"
    )
    private Long semesterId;

    @Schema(
            description = "Semester Name",
            example = "Semester II"
    )
    private String semesterName;

    // ==========================
    // Marks
    // ==========================

    @Schema(
            description = "Marks obtained by the student",
            example = "85"
    )
    private Integer obtainedMarks;

    @Schema(
            description = "Maximum marks of the examination",
            example = "100"
    )
    private Integer maximumMarks;

    // ==========================
    // Calculated Fields
    // ==========================

    @Schema(
            description = "Calculated percentage",
            example = "85.00"
    )
    private BigDecimal percentage;

    @Schema(
            description = "Calculated grade",
            example = "A"
    )
    private String grade;

    @Schema(
            description = "Final examination result",
            example = "PASS"
    )
    private ResultStatus resultStatus;

    // ==========================
    // Remarks
    // ==========================

    @Schema(
            description = "Teacher remarks",
            example = "Excellent Performance"
    )
    private String remarks;

    // ==========================
    // Record Status
    // ==========================

    @Schema(
            description = "Current record status",
            example = "ACTIVE",
            allowableValues = {
                    "ACTIVE",
                    "INACTIVE",
                    "DELETED"
            }
    )
    private RecordStatus status;

    // ==========================
    // Audit Fields
    // ==========================

    @Schema(
            description = "Record creation timestamp",
            example = "2026-07-15T10:30:45"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Record last updated timestamp",
            example = "2026-07-15T14:20:15"
    )
    private LocalDateTime updatedAt;
    
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
	 // Update Information
	 // =====================================================
	
	 @Schema(
	         description = "Updated By User ID",
	         example = "102"
	 )
	 private Long updatedByUserId;
	
	
	 @Schema(
	         description = "Updated By",
	         example = "Admin"
	 )
	 private String updatedBy;
	
	
	 @Schema(
	         description = "Updated Date",
	         example = "2026-07-24T11:30:00"
	 )
	 private LocalDateTime updatedDate;

}