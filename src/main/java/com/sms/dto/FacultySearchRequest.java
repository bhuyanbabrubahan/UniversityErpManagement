package com.sms.dto;

import com.sms.enums.Designation;
import com.sms.enums.Qualification;
import com.sms.enums.RecordStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacultySearchRequest {
	private Long id;
	private String employeeCode;
	private String firstName;
	private String lastName;
	private String fullName;
	private Long departmentId;
	private Designation designation;
	private Qualification qualification;
	private RecordStatus status;
}
