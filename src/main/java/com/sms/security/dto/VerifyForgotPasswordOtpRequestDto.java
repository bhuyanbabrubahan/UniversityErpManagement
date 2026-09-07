package com.sms.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "VerifyForgotPasswordOtpRequestDto",
        description = "Request DTO to verify forgot password OTP."
)
public class VerifyForgotPasswordOtpRequestDto {

    @Schema(
            description = "Registered mobile number",
            example = "9876543210"
    )
    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter valid 10 digit mobile number"
    )
    private String mobileNumber;

    @Schema(
            description = "6 digit OTP",
            example = "123456"
    )
    @NotBlank(message = "OTP is required")
    @Pattern(
            regexp = "^\\d{6}$",
            message = "OTP must be 6 digits"
    )
    private String otp;

}