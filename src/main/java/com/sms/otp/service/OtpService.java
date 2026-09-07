package com.sms.otp.service;

import com.sms.otp.enums.OtpPurpose;

public interface OtpService {


    String sendOtp(
            String mobileNumber,
            OtpPurpose purpose
    );


    String verifyOtp(
            String mobileNumber,
            String otp,
            OtpPurpose purpose
    );


    String resendOtp(
            String mobileNumber,
            OtpPurpose purpose
    );

}