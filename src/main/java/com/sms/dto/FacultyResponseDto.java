package com.sms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.location.address.enums.AddressType;
import com.sms.enums.Designation;
import com.sms.enums.Gender;
import com.sms.enums.Qualification;
import com.sms.enums.RecordStatus;
import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FacultyResponseDto", description = "Response DTO containing complete faculty information.")
public class FacultyResponseDto {

	private Long id;
	private Long userId;
	private String employeeCode;

	private String firstName;
	private String lastName;
	private String middleName;
	private String fullName;

	private Gender gender;
	private LocalDate dateOfBirth;
	private LocalDate joiningDate;

	private Designation designation;
	private Qualification qualification;
	private BigDecimal salary;

	private String profileImage;
	private String signatureImage;

	private String email;
	private String mobileNumber;

	private Long departmentId;
	private String departmentName;

	private RecordStatus status;
	private UserStatus userStatus;
	private Role role;

	private Long addressId;
	private String houseNumber;
	private String street;
	private String landmark;
	private AddressType addressType;

	private Long villageId;
	private String villageName;
	private Long tehsilId;
	private String tehsilName;
	private Long districtId;
	private String districtName;
	private Long stateId;
	private String stateName;
	private Long countryId;
	private String countryName;
	private String pincode;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Long createdByUserId;
	private String createdBy;
	private LocalDateTime createdDate;

	private Long updatedByUserId;
	private String updatedBy;
	private LocalDateTime updatedDate;
}

