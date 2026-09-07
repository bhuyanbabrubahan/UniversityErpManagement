package com.sms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "FacultySubjectResponseDto",
    description = "Response DTO containing faculty-subject assignment details."
)
public class FacultySubjectResponseDto {

    @Schema(
        description = "Unique Faculty Subject Mapping ID",
        example = "1"
    )
    private Long id;

    @Schema(
        description = "Faculty ID",
        example = "10"
    )
    private Long facultyId;

    @Schema(
        description = "Faculty full name",
        example = "Rahul Sharma"
    )
    private String facultyName;

    @Schema(
        description = "Subject ID",
        example = "5"
    )
    private Long subjectId;

    @Schema(
        description = "Subject name",
        example = "Data Structures"
    )
    private String subjectName;

    @Schema(
        description = "Date on which the subject was assigned to the faculty",
        example = "2026-07-15"
    )
    private LocalDate assignedDate;

    @Schema(
        description = "Academic year of the faculty-subject assignment",
        example = "2026-2027"
    )
    private String academicYear;

    @Schema(
        description = "Current record status",
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
 	         description = "Created At",
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
 	         description = "Updated At",
 	         example = "2026-07-24T11:30:00"
 	 )
 	 private LocalDateTime updatedAt;
    
    
    

}