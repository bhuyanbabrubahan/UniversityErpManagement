package com.sms.security.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.location.address.enums.AddressType;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.security.enums.AdminDesignation;
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
@Schema(name = "AdminResponseDto", description = "Response DTO containing complete admin information.")
public class AdminResponseDto {

	private Long id;
	private Long userId;
	private String employeeCode;

	private String firstName;
	private String lastName;
	private String fullName;

	private Gender gender;
	private LocalDate dateOfBirth;

	private AdminDesignation designation;
	private LocalDate joiningDate;
	private BigDecimal salary;
	private String remarks;

	private String profileImage;
	private String signatureImage;

	private String email;
	private String mobileNumber;

	private Role role;
	private UserStatus userStatus;
	private RecordStatus status;

	private String houseNumber;
	private String street;
	private String landmark;
	private AddressType addressType;

	private Long villageId;
	private String village;
	private Long tehsilId;
	private String tehsil;
	private Long districtId;
	private String district;
	private Long stateId;
	private String state;
	private Long countryId;
	private String country;
	private String pincode;

	private Long createdByUserId;
	private String createdBy;
	private LocalDateTime createdDate;

	private Long updatedByUserId;
	private String updatedBy;
	private LocalDateTime updatedDate;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
