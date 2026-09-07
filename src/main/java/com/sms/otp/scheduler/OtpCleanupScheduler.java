package com.sms.otp.scheduler;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.otp.entity.OtpVerification;
import com.sms.otp.repository.OtpVerificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Component
@RequiredArgsConstructor
@Slf4j
public class OtpCleanupScheduler {



    private final OtpVerificationRepository otpRepository;



    // =====================================
    // OTP CLEANUP JOB
    // Runs every 10 minutes
    // =====================================

    @Scheduled(
            fixedRate = 600000
    )
    @Transactional
    public void cleanupExpiredOtp(){



        List<OtpVerification> expiredOtpList =
                otpRepository
                .findByExpiryTimeBeforeAndStatus(
                        LocalDateTime.now(),
                        RecordStatus.ACTIVE
                );



        if(expiredOtpList.isEmpty()){

            log.info(
                "No expired OTP found."
            );

            return;
        }



        for(OtpVerification otp :
                expiredOtpList){


            otp.setStatus(
                    RecordStatus.INACTIVE
            );

        }



        otpRepository.saveAll(
                expiredOtpList
        );



        log.info(
            "{} expired OTP marked inactive.",
            expiredOtpList.size()
        );

    }

}