package com.admission.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.admission.enums.AdmissionStatus;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdmissionResponseDto {

	@Schema(description = "Admission ID", example = "1")
	private Long id;

	@Schema(description = "Admission Number", example = "ADM2026001")
	private String admissionNumber;

	@Schema(description = "Student ID", example = "1001")
	private Long studentId;

	@Schema(description = "Student Name", example = "Rahul Kumar")
	private String studentName;

	@Schema(description = "Student Roll Number", example = "ROLL1001")
	private String studentRollNumber;

	@Schema(description = "Department ID", example = "10")
	private Long departmentId;

	@Schema(description = "Department Name", example = "Computer Science")
	private String departmentName;

	@Schema(description = "Course ID", example = "20")
	private Long courseId;

	@Schema(description = "Course Name", example = "B.Tech Computer Science")
	private String courseName;

	@Schema(description = "Academic Year", example = "2026-2027")
	private String academicYear;

	@Schema(description = "Semester ID", example = "1")
	private Long semesterId;

	@Schema(description = "Semester Name", example = "Semester 1")
	private String semesterName;

	@Schema(description = "Admission Date", example = "2026-07-24")
	private LocalDate admissionDate;

	@Schema(description = "Admission Status", example = "ACTIVE")
	private AdmissionStatus admissionStatus;
	
	@Schema(description = "Record Status", example = "ACTIVE")
	private RecordStatus status;

	@Schema(description = "Additional Remarks", example = "Regular admission")
	private String remarks;


	// =====================================================
	// Creation Information
	// =====================================================

	@Schema(description = "Created By User ID", example = "101")
	private Long createdByUserId;

	@Schema(description = "Created By", example = "Admin")
	private String createdBy;

	@Schema(description = "Created Date", example = "2026-07-24T10:30:00")
	private LocalDateTime createdAt;

	// =====================================================
	// Update Information
	// =====================================================

	@Schema(description = "Updated By User ID", example = "102")
	private Long updatedByUserId;

	@Schema(description = "Updated By", example = "Admin")
	private String updatedBy;

	@Schema(description = "Updated Date", example = "2026-07-24T11:30:00")
	private LocalDateTime updatedAt;
}