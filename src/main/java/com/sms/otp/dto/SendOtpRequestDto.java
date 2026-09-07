package com.sms.otp.dto;


import com.sms.otp.enums.OtpPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SendOtpRequestDto {


    @Schema(
            example = "9876543210",
            description = "User mobile number"
    )
    @NotBlank(
            message = "Mobile number is required"
    )
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter valid 10 digit mobile number"
    )
    private String mobileNumber;



    @Schema(
            example = "LOGIN",
            description = "OTP purpose"
    )
    @NotNull(
            message = "OTP purpose is required"
    )
    private OtpPurpose purpose;




}