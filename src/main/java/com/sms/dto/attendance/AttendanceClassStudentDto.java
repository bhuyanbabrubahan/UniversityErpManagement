package com.sms.dto.attendance;

import com.sms.enums.AttendanceStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceClassStudentDto {

	private Long studentId;

	private String rollNumber;

	private String studentCode;

	private String studentName;

	private String departmentName;

	private String courseName;

	private String semesterName;

	private String section;

	private String batch;

	// Existing attendance, if already marked
	private Long attendanceId;

	private AttendanceStatus attendanceStatus;

	private String remarks;
}

