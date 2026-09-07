package com.sms.security.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.location.address.dto.AddressRequestDto;
import com.sms.enums.Gender;
import com.sms.security.enums.SuperAdminDesignation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperAdminRequestDto {

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;
	
	@NotBlank(message = "Mobile number is required")
	@Pattern(
	 regexp="^[6-9]\\d{9}$",
	 message="Invalid mobile number"
	)
	private String mobileNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

	@NotBlank(message = "Password is required")
	@Pattern(
	        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
	        message = "Password must contain uppercase, lowercase, number and special character."
	)
	private String password;
    
    @Schema(
            description = "Super Admin profile image",
            example = "images/super_admin1.jpg"
        )
    private String profileImage;
    
    @Schema(
            description = "Super Admin gender",
            example = "MALE",
            allowableValues = {
                "MALE",
                "FEMALE",
                "OTHER"
            },
            requiredMode = Schema.RequiredMode.REQUIRED
        )
    @NotNull(message = "Gender is required")
    private Gender gender;


    @NotNull(message = "Designation is required")
    private SuperAdminDesignation designation;

	private LocalDate joiningDate;

	private LocalDate dateOfBirth;

	private BigDecimal salary;

	private String remarks;
	
    @Schema(
            description = "Admin address details",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
	@NotNull(message = "Address is required")
	@Valid
	private AddressRequestDto address;

}
