package com.sms.otp.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sms.otp.service.SmsService;


@Service
public class MockSmsServiceImpl
        implements SmsService {


    private static final Logger log =
            LoggerFactory.getLogger(
                    MockSmsServiceImpl.class
            );


    @Override
    public void sendOtp(
            String mobileNumber,
            String otp
    ) {


        log.info(
                """
                
                ==========================
                MOCK SMS SENT
                ==========================
                Mobile : {}
                OTP    : {}
                ==========================
                
                """,
                mobileNumber,
                otp
        );

    }

}