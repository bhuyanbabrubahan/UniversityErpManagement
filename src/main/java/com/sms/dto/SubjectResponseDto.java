package com.sms.dto;

import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(
        description = "Subject Response"
)
@Getter
@Setter
public class SubjectResponseDto {
	
	@Schema(example = "1")
    private Long id;

	@Schema(example = "SUB001")
    private String subjectCode;

	@Schema(example = "Advanced Java Programming")
    private String subjectName;

	@Schema(example = "4")
    private Integer credits;

	@Schema(example = "70")
    private Integer theoryMarks;

	@Schema(example = "30")
    private Integer practicalMarks;

	@Schema(example = "1")
    private Long semesterId;

	@Schema(example = "Semester 1")
    private String semesterName;

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