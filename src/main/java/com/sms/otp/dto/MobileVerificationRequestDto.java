package com.sms.otp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "MobileVerificationRequestDto", description = "Request DTO for mobile verification OTP.")
public class MobileVerificationRequestDto {

	@Schema(description = "Registered mobile number", example = "9876543210")
	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter valid 10 digit mobile number")
	private String mobileNumber;

}