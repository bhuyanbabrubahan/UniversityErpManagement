package com.admission.dto;

import com.admission.enums.AdmissionStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdmissionSearchRequest {

	// Admission unique number search
	private String admissionNumber;

	// Student based search
	private Long studentId;

	// Student name search
	private String studentName;

	// Department filter
	private Long departmentId;

	// Course filter
	private Long courseId;

	// Academic year filter
	private String academicYear;

	// Semester filter
	private Long semesterId;

	// Admission lifecycle filter
	private AdmissionStatus admissionStatus;

}