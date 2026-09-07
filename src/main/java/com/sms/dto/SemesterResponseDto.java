package com.sms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(
        name = "Semester Response",
        description = "Semester Response DTO"
)
public class SemesterResponseDto {

    @Schema(
            description = "Semester Id",
            example = "1"
    )
    private Long id;

    // ==========================
    // Semester Details
    // ==========================

    @Schema(
            description = "Semester Name",
            example = "Semester 1"
    )
    private String semesterName;

    @Schema(
            description = "Semester Number",
            example = "1"
    )
    private Integer semesterNumber;

    @Schema(
            description = "Semester Start Date",
            example = "2026-01-01"
    )
    private LocalDate semesterStartDate;

    @Schema(
            description = "Semester End Date",
            example = "2026-06-30"
    )
    private LocalDate semesterEndDate;

    @Schema(
            description = "Automatically calculated total working days in the semester",
            example = "154"
    )
    private Integer totalWorkingDays;

    // ==========================
    // Course Details
    // ==========================

    @Schema(
            description = "Course Id",
            example = "1"
    )
    private Long courseId;

    @Schema(
            description = "Course Code",
            example = "CSE101"
    )
    private String courseCode;

    @Schema(
            description = "Course Name",
            example = "Java Full Stack"
    )
    private String courseName;

    // ==========================
    // Status
    // ==========================

    @Schema(
            description = "Record Status",
            example = "ACTIVE"
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
	 private LocalDateTime createdAt;
	
	
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
	 private LocalDateTime updatedAt;

}