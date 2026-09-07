package com.sms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.location.address.dto.AddressResponseDto;
import com.sms.enums.BloodGroup;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.enums.StudentCategory;
import com.sms.enums.StudentStatus;
import com.sms.enums.StudentType;
import com.sms.security.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "StudentResponseDto", description = "Response DTO containing complete student information.")
public class StudentResponseDto {

// =====================================================
// STUDENT IDENTITY
// =====================================================

	@Schema(description = "Unique student ID", example = "1")
	private Long id;

	@Schema(description = "Unique student code", example = "STD20260001")
	private String studentCode;

	@Schema(description = "Student roll number", example = "ROLL001")
	private String rollNumber;

	@Schema(description = "Student registration number", example = "REG20260001")
	private String registrationNumber;

	@Schema(description = "Student admission number", example = "ADM20260001")
	private String admissionNumber;

// =====================================================
// PERSONAL INFORMATION
// =====================================================

	@Schema(description = "Student first name", example = "Rahul")
	private String firstName;

	@Schema(description = "Student middle name", example = "Kumar")
	private String middleName;

	@Schema(description = "Student last name", example = "Sharma")
	private String lastName;

	@Schema(description = "Student full name", example = "Rahul Kumar Sharma")
	private String fullName;

	@Schema(description = "Student gender")
	private Gender gender;

	@Schema(description = "Date of birth", example = "2004-05-20")
	private LocalDate dateOfBirth;

	@Schema(description = "Student blood group", example = "A_POSITIVE")
	private BloodGroup bloodGroup;

	@Schema(description = "Student category", example = "GENERAL")
	private StudentCategory category;

	@Schema(description = "Student religion", example = "Hindu")
	private String religion;

	@Schema(description = "Student nationality", example = "Indian")
	private String nationality;

// =====================================================
// CONTACT INFORMATION
// =====================================================

	@Schema(description = "Student user account information")
	private UserResponseDto user;

	@Schema(description = "Alternate mobile number", example = "9876543210")
	private String alternateMobile;

	@Schema(description = "Alternate email", example = "student@gmail.com")
	private String alternateEmail;

// =====================================================
// IDENTITY DOCUMENTS
// =====================================================

	@Schema(description = "Aadhaar number", example = "123456789012")
	private String aadhaarNumber;

	@Schema(description = "PAN number", example = "ABCDE1234F")
	private String panNumber;

	@Schema(description = "Signature image path")
	private String signatureImage;

// =====================================================
// PROFILE
// =====================================================

	@Schema(description = "Student profile image", example = "images/student1.jpg")
	private String profileImage;

// =====================================================
// ACADEMIC INFORMATION
// =====================================================

	@Schema(description = "Admission date", example = "2026-07-15")
	private LocalDate admissionDate;

	@Schema(description = "Academic year", example = "2026-2027")
	private String academicYear;

	@Schema(description = "Student department")
	private DepartmentResponseDto department;

	@Schema(description = "Student course")
	private CourseResponseDto course;

	@Schema(description = "Current semester")
	private SemesterResponseDto currentSemester;

	@Schema(description = "Student section", example = "A")
	private String section;

	@Schema(description = "Student batch", example = "2026")
	private String batch;

	@Schema(description = "Current academic year", example = "1")
	private Integer currentYear;

	@Schema(description = "Student type")
	private StudentType studentType;

	@Schema(description = "Student status")
	private StudentStatus studentStatus;

// =====================================================
// ADDRESS INFORMATION
// =====================================================

	@Schema(description = "Student address information")
	private AddressResponseDto address;

// =====================================================
// RECORD STATUS
// =====================================================

	@Schema(description = "Current record status", example = "ACTIVE")
	private RecordStatus status;

// =====================================================
// AUDIT INFORMATION
// =====================================================

	@Schema(description = "Created By User ID", example = "101")
	private Long createdByUserId;

	@Schema(description = "Created By", example = "Admin")
	private String createdBy;

	@Schema(description = "Created At", example = "2026-07-24T10:30:00")
	private LocalDateTime createdAt;

	@Schema(description = "Updated By User ID", example = "102")
	private Long updatedByUserId;

	@Schema(description = "Updated By", example = "Admin")
	private String updatedBy;

	@Schema(description = "Updated At", example = "2026-07-24T11:30:00")
	private LocalDateTime updatedAt;

}