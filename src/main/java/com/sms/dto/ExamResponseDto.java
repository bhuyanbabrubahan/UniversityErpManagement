package com.sms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.sms.enums.ExamType;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "ExamResponseDto",
    description = "Response DTO containing examination details."
)
public class ExamResponseDto {

    // ==========================
    // Primary Key
    // ==========================

    @Schema(
        description = "Unique exam ID",
        example = "1"
    )
    private Long id;

    // ==========================
    // Exam Information
    // ==========================

    @Schema(
        description = "Exam name",
        example = "Mid Semester Examination"
    )
    private String examName;

    @Schema(
        description = "Type of examination",
        example = "THEORY",
        allowableValues = {
            "THEORY",
            "PRACTICAL",
            "VIVA"
        }
    )
    private ExamType examType;

    @Schema(
        description = "Scheduled exam date",
        example = "2026-12-15"
    )
    private LocalDate examDate;

    @Schema(
        description = "Exam start time",
        example = "10:00"
    )
    private LocalTime startTime;

    @Schema(
        description = "Exam end time",
        example = "13:00"
    )
    private LocalTime endTime;

    // ==========================
    // Marks
    // ==========================

    @Schema(
        description = "Maximum marks",
        example = "100"
    )
    private Integer maximumMarks;

    @Schema(
        description = "Passing marks",
        example = "40"
    )
    private Integer passingMarks;

    // ==========================
    // Academic Year
    // ==========================

    @Schema(
        description = "Academic year",
        example = "2026-2027"
    )
    private String academicYear;

    // ==========================
    // Subject
    // ==========================

    @Schema(
        description = "Subject ID",
        example = "5"
    )
    private Long subjectId;

    @Schema(
        description = "Subject name",
        example = "Object Oriented Programming"
    )
    private String subjectName;

    // ==========================
    // Semester
    // ==========================

    @Schema(
        description = "Semester ID",
        example = "2"
    )
    private Long semesterId;

    @Schema(
        description = "Semester name",
        example = "Semester II"
    )
    private String semesterName;

    // ==========================
    // Status
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