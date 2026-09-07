package com.sms.otp.dto;


import com.sms.otp.enums.OtpPurpose;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResendOtpRequestDto {


    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter valid mobile number"
    )
    private String mobileNumber;



    @NotNull(message = "OTP purpose is required")
    private OtpPurpose purpose;

}