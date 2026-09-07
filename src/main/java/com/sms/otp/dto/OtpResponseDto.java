package com.sms.otp.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class OtpResponseDto {


    private String mobileNumber;


    private String message;


    private LocalDateTime timestamp;

}