package com.sms.otp.dto;


import com.sms.otp.enums.OtpPurpose;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VerifyOtpRequestDto {


    @NotBlank(
            message = "Mobile number is required"
    )
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter valid 10 digit mobile number"
    )
    private String mobileNumber;



    @NotBlank(
            message = "OTP is required"
    )
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "OTP must be 6 digits"
    )
    private String otp;



    @NotNull(
            message = "OTP purpose is required"
    )
    private OtpPurpose purpose;

}