package com.sms.otp.serviceImpl;


import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.otp.entity.OtpVerification;
import com.sms.otp.enums.OtpPurpose;
import com.sms.otp.repository.OtpVerificationRepository;
import com.sms.otp.service.OtpService;
import com.sms.otp.service.PasswordResetTokenService;
import com.sms.otp.service.SmsService;
import com.sms.otp.util.OtpGeneratorUtil;
import com.sms.security.entity.User;
import com.sms.security.enums.UserStatus;
import com.sms.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class OtpServiceImpl implements OtpService {


	private final OtpVerificationRepository otpRepository;
	private final UserRepository userRepository;
	private final SmsService smsService;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenService passwordResetTokenService;

    @Override
    @Transactional
    public String sendOtp(
            String mobileNumber,
            OtpPurpose purpose
    ) {

        User user = userRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
        

        // Mobile verification ke liye hi check
        if (purpose == OtpPurpose.MOBILE_VERIFICATION
                && Boolean.TRUE.equals(user.getMobileVerified())) {

            throw new BusinessException(
                    "Mobile number already verified."
            );
        }

        // Last OTP
        OtpVerification oldOtp =
                otpRepository
                .findTopByUserIdAndPurposeOrderByCreatedAtDesc(
                        user.getId(),
                        purpose
                )
                .orElse(null);

        if (oldOtp != null) {

            /*
             * Apply cooldown only when
             * previous OTP is still pending.
             */
            if (!Boolean.TRUE.equals(oldOtp.getVerified())) {

                LocalDateTime resendAllowedTime =
                        oldOtp.getCreatedAt().plusSeconds(60);

                if (LocalDateTime.now().isBefore(resendAllowedTime)) {

                    long remaining =
                            java.time.Duration
                                    .between(
                                            LocalDateTime.now(),
                                            resendAllowedTime
                                    )
                                    .getSeconds();

                    throw new BusinessException(
                            "Please wait "
                                    + remaining
                                    + " seconds before requesting a new OTP."
                    );
                }
            }

            /*
             * Inactivate previous OTP.
             */
            oldOtp.setStatus(RecordStatus.INACTIVE);
            otpRepository.save(oldOtp);
        }

        // Generate OTP
        String otp = OtpGeneratorUtil.generateOtp();
        OtpVerification otpVerification =
                new OtpVerification();
        otpVerification.setUser(user);
        otpVerification.setMobileNumber(mobileNumber);
        otpVerification.setOtpHash(
                passwordEncoder.encode(otp)
        );

        otpVerification.setPurpose(purpose);
        otpVerification.setExpiryTime(
                LocalDateTime.now().plusMinutes(5)
        );

        otpVerification.setAttemptCount(0);
        otpVerification.setVerified(false);
        otpVerification.setStatus(RecordStatus.ACTIVE);

        otpRepository.save(otpVerification);

        smsService.sendOtp(
                mobileNumber,
                otp
        );

        return "OTP sent successfully";
    }


    @Override
    @Transactional
    public String verifyOtp(
            String mobileNumber,
            String otp,
            OtpPurpose purpose
    ) {


        // =====================================
        // 1. Find Latest OTP
        // =====================================

        OtpVerification otpVerification =

                otpRepository
                .findTopByMobileNumberAndPurposeOrderByCreatedAtDesc(
                        mobileNumber,
                        purpose
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "OTP not found. Please request new OTP."
                        )
                );



        // =====================================
        // 2. Check OTP Already Verified
        // =====================================

        if(Boolean.TRUE.equals(
                otpVerification.getVerified()
        )) {


            throw new BusinessException(
                    "OTP already verified."
            );

        }



        // =====================================
        // 3. Check OTP Status
        // =====================================

        if(otpVerification.getStatus()
                != RecordStatus.ACTIVE) {


            throw new BusinessException(
                    "OTP expired or already used. Please request new OTP."
            );

        }



        // =====================================
        // 4. Check Expiry Time
        // =====================================

        if(otpVerification.getExpiryTime()
                .isBefore(LocalDateTime.now())) {


            otpVerification.setStatus(
                    RecordStatus.INACTIVE
            );


            otpRepository.save(
                    otpVerification
            );


            throw new BusinessException(
                    "OTP expired. Please request new OTP."
            );

        }



        // =====================================
        // 5. Maximum Attempt Check
        // =====================================

        if(otpVerification.getAttemptCount() >= 5) {

            otpVerification.setStatus(
                    RecordStatus.INACTIVE
            );

            otpRepository.save(
                    otpVerification
            );

            throw new BusinessException(
                    "Maximum OTP attempts exceeded. Please request new OTP."
            );

        }



        // =====================================
        // 6. Verify OTP Hash
        // =====================================

        boolean matched =
                passwordEncoder.matches(
                        otp,
                        otpVerification.getOtpHash()
                );



        if(!matched) {


            otpVerification.setAttemptCount(
                    otpVerification.getAttemptCount() + 1
            );


            otpRepository.save(
                    otpVerification
            );


            throw new BusinessException(

                    "Invalid OTP. Attempt "
                    + otpVerification.getAttemptCount()
                    + " of 5"

            );

        }



        // =====================================
        // 7. OTP Success
        // =====================================

		otpVerification.setVerified(true);
		otpVerification.setStatus(RecordStatus.INACTIVE);
		otpRepository.save(otpVerification);
		String resetToken = null;

         // Password reset flow.
         // After successful OTP verification,
         //generate temporary password reset token.
        if(purpose == OtpPurpose.FORGOT_PASSWORD) {


            resetToken =
                    passwordResetTokenService.createToken(
                            otpVerification.getUser()
                    );

        }

        // =====================================
        // 8. Update Mobile Verification
        // Only MOBILE_VERIFICATION OTP
        // =====================================

		if (purpose == OtpPurpose.MOBILE_VERIFICATION) {

			User user = otpVerification.getUser();
			user.setMobileVerified(true);
			if (Boolean.TRUE.equals(user.getEmailVerified())) {

				user.setStatus(UserStatus.ACTIVE);

			} else {

				user.setStatus(UserStatus.PENDING_VERIFICATION);

			}

			userRepository.save(user);
		}

		if (purpose == OtpPurpose.FORGOT_PASSWORD) {
			return resetToken;
		}
		return "OTP verified successfully";

	}





    @Override
    @Transactional
    public String resendOtp(
            String mobileNumber,
            OtpPurpose purpose
    ) {


        // =====================================
        // 1. Validate User
        // =====================================

        User user =
                userRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );



        // =====================================
        // 2. Check Mobile Already Verified
        // =====================================

        if(purpose == OtpPurpose.MOBILE_VERIFICATION
                && Boolean.TRUE.equals(user.getMobileVerified())) {


            throw new BusinessException(
                "Mobile number already verified."
            );

        }



        // =====================================
        // 3. Find Latest OTP
        // =====================================

        OtpVerification oldOtp =
                otpRepository
                .findTopByUserIdAndPurposeOrderByCreatedAtDesc(
                        user.getId(),
                        purpose
                )
                .orElse(null);



        if(oldOtp != null) {


            // =====================================
            // Expire Old OTP
            // =====================================

            if(oldOtp.getExpiryTime()
                    .isBefore(LocalDateTime.now())) {


                oldOtp.setStatus(
                        RecordStatus.INACTIVE
                );

                otpRepository.save(oldOtp);

            }



            // =====================================
            // Resend Cooldown
            // =====================================

            LocalDateTime resendAllowedTime =
                    oldOtp.getCreatedAt()
                    .plusSeconds(60);



            if(LocalDateTime.now()
                    .isBefore(resendAllowedTime)) {


                long remainingSeconds =
                        java.time.Duration
                        .between(
                                LocalDateTime.now(),
                                resendAllowedTime
                        )
                        .getSeconds();


                throw new BusinessException(
                        "Please wait "
                        + remainingSeconds
                        + " seconds before requesting new OTP."
                );
            }



            // =====================================
            // Invalidate Previous OTP
            // =====================================

            oldOtp.setStatus(
                    RecordStatus.INACTIVE
            );

            otpRepository.save(oldOtp);

        }



        // =====================================
        // 4. Generate New OTP
        // =====================================

        String otp =
                OtpGeneratorUtil.generateOtp();



        // =====================================
        // 5. Save New OTP
        // =====================================

        OtpVerification otpVerification =
                new OtpVerification();


        otpVerification.setUser(user);


        otpVerification.setMobileNumber(
                mobileNumber
        );


        otpVerification.setOtpHash(
                passwordEncoder.encode(otp)
        );


        otpVerification.setPurpose(
                purpose
        );


        otpVerification.setExpiryTime(
                LocalDateTime.now()
                .plusMinutes(5)
        );


        otpVerification.setAttemptCount(0);


        otpVerification.setVerified(false);


        otpVerification.setStatus(
                RecordStatus.ACTIVE
        );


        otpRepository.save(
                otpVerification
        );



        // =====================================
        // 6. Send SMS
        // =====================================

        smsService.sendOtp(
                mobileNumber,
                otp
        );


        return "OTP resent successfully";

    }

}