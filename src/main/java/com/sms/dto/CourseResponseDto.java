package com.sms.dto;

import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

	@Schema(description = "Course Id", example = "1")
	private Long id;

	@Schema(description = "Course Code", example = "CSE101")
	private String courseCode;

	@Schema(description = "Course Name", example = "Java Full Stack")
	private String courseName;

	@Schema(description = "Duration In Months", example = "48")
	private Integer durationInMonths;

	@Schema(description = "Description", example = "Bachelor of Technology in Electronics Engineering")
	private String description;

	@Schema(description = "Status", example = "ACTIVE")
	private RecordStatus status;

	// 🔥 relationship output (important for frontend)
	@Schema(description = "Department Id", example = "1")
	private Long departmentId;

	@Schema(description = "Department Name", example = "Computer Science")
	private String departmentName;


	// Creation Information

	@Schema(description = "Created By User ID", example = "101")
	private Long createdByUserId;

	@Schema(description = "Created By", example = "Admin")
	private String createdBy;

	@Schema(description = "Course creation timestamp", example = "2026-07-12T11:33:17.735736")
	private LocalDateTime createdAt;

	// Update Information

	@Schema(description = "Updated By User ID", example = "102")
	private Long updatedByUserId;

	@Schema(description = "Updated By", example = "Admin")
	private String updatedBy;

	@Schema(description = "Course last updated timestamp", example = "2026-07-12T11:42:20.458126")
	private LocalDateTime updatedAt;

}