package com.sms.otp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.otp.dto.ResendOtpRequestDto;
import com.sms.otp.dto.SendOtpRequestDto;
import com.sms.otp.dto.VerifyOtpRequestDto;
import com.sms.otp.service.OtpService;
import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDto<String>> sendOtp(
            @Valid @RequestBody SendOtpRequestDto request) {

        String response = otpService.sendOtp(
                request.getMobileNumber(),
                request.getPurpose()
        );

        return ResponseBuilder.success(
                response,
                "OTP sent successfully.",
                HttpStatus.OK
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<String>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDto request) {

        String response = otpService.verifyOtp(
                request.getMobileNumber(),
                request.getOtp(),
                request.getPurpose()
        );

        return ResponseBuilder.success(
                response,
                "OTP verified successfully.",
                HttpStatus.OK
        );
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponseDto<String>> resendOtp(
            @Valid @RequestBody ResendOtpRequestDto request) {

        String response = otpService.resendOtp(
                request.getMobileNumber(),
                request.getPurpose()
        );

        return ResponseBuilder.success(
                response,
                "OTP resent successfully.",
                HttpStatus.OK
        );
    }
}