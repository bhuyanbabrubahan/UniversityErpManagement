package com.sms.security.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.location.address.enums.AddressType;
import com.sms.enums.Gender;
import com.sms.enums.RecordStatus;
import com.sms.security.enums.Role;
import com.sms.security.enums.SuperAdminDesignation;

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
public class SuperAdminResponseDto {


    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

	private String mobileNumber;

	private SuperAdminDesignation designation;

	private String profileImage;
	
    @Schema(
            description = "Gender",
            example = "MALE"
        )
    private Gender gender;

    private LocalDate dateOfBirth;

    private BigDecimal salary;

    private LocalDate joiningDate;

    private String remarks;

    private Role role;

    private RecordStatus status;
    
    // =========================
    // Address
    // =========================

    @Schema(
        description = "House Number",
        example = "12A"
    )
    private String houseNumber;

    @Schema(
        description = "Street",
        example = "University Road"
    )
    private String street;

    @Schema(
        description = "Landmark",
        example = "Near SBI Bank"
    )
    private String landmark;

    @Schema(
        description = "Address Type",
        example = "PERMANENT"
    )
    private AddressType addressType;

    @Schema(
        description = "Village",
        example = "Gangadahani"
    )
    private String village;

    @Schema(
        description = "Tehsil",
        example = "Aska"
    )
    private String tehsil;

    @Schema(
        description = "District",
        example = "Ganjam"
    )
    private String district;

    @Schema(
        description = "State",
        example = "Odisha"
    )
    private String state;

    @Schema(
        description = "Country",
        example = "India"
    )
    private String country;

    @Schema(
        description = "Postal Code",
        example = "761018"
    )
    private String pincode;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}