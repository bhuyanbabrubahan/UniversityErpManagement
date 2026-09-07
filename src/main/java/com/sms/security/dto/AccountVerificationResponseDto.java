package com.sms.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountVerificationResponseDto {

    private String email;

    private String mobileNumber;

    private Boolean emailVerified;

    private Boolean mobileVerified;

    private String status;

}