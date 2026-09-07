package com.sms.dto;

import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.BloodGroup;
import com.sms.enums.Gender;
import com.sms.enums.StudentCategory;
import com.sms.enums.StudentStatus;
import com.sms.enums.StudentType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "StudentRequestDto", description = "Request DTO used for creating and updating student information.")
public class StudentRequestDto {

// USER ACCOUNT INFORMATION

	@Schema(description = "Student email address", example = "rahul@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Schema(description = "Student account password", example = "Rahul@123")
	@Pattern(regexp = "^$|^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$", message = "Password must contain uppercase, lowercase, number and special character")
	private String password;

	private String confirmPassword;

	@Schema(description = "Student mobile number", example = "9876543210", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10 digit Indian mobile number")
	private String mobileNumber;

// PERSONAL INFORMATION

	@Schema(description = "Student first name", example = "Rahul", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "First name is required")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstName;

	@Schema(description = "Student middle name", example = "Kumar")
	private String middleName;

	@Schema(description = "Student last name", example = "Sharma", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Last name is required")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastName;

	@Schema(description = "Student gender", example = "MALE")
	@NotNull(message = "Gender is required")
	private Gender gender;

	@Schema(description = "Student date of birth", example = "2004-05-20")
	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	private LocalDate dateOfBirth;

	private BloodGroup bloodGroup;

	private StudentCategory category;

	private String religion;

	private String nationality;

// CONTACT INFORMATION

	private String alternateMobile;

	@Email(message = "Invalid alternate email format")
	private String alternateEmail;

// DOCUMENT INFORMATION

	private String aadhaarNumber;

	private String panNumber;

// PROFILE INFORMATION

	private String profileImage;

	private String signatureImage;

// ADMISSION INFORMATION

	@Schema(description = "Registration number", example = "REG20260001")
	private String registrationNumber;

	@Schema(description = "Admission number", example = "ADM20260001")
	private String admissionNumber;

	@Schema(description = "Student admission date", example = "2026-07-15", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "Admission date is required")
	private LocalDate admissionDate;

	private String academicYear;

// ACADEMIC INFORMATION

	@NotNull(message = "Department is required")
	private Long departmentId;

	private Long courseId;

	private Long semesterId;

	private String section;

	private String batch;

	private Integer currentYear;

	private StudentType studentType;

	private StudentStatus studentStatus;

// ADDRESS INFORMATION

	@NotNull(message = "Address is required")
	@Valid
	private AddressRequestDto address;

}