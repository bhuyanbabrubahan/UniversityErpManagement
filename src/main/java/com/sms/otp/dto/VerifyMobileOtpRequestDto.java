package com.sms.otp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "VerifyMobileOtpRequestDto",
        description = "Request DTO for verifying mobile OTP."
)
public class VerifyMobileOtpRequestDto {


    @NotBlank(
            message = "Mobile number is required"
    )
    @Pattern(
            regexp="^[6-9]\\d{9}$",
            message="Enter valid 10 digit mobile number"
    )
    private String mobileNumber;



    @NotBlank(
            message = "OTP is required"
    )
    @Pattern(
            regexp="^\\d{6}$",
            message="OTP must be 6 digits"
    )
    private String otp;

}