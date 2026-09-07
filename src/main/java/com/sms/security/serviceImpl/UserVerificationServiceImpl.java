package com.sms.security.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.email.entity.EmailVerificationToken;
import com.sms.email.repository.EmailVerificationTokenRepository;
import com.sms.email.service.EmailService;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.otp.enums.OtpPurpose;
import com.sms.otp.service.OtpService;
import com.sms.security.entity.User;
import com.sms.security.enums.UserStatus;
import com.sms.security.service.UserVerificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserVerificationServiceImpl
        implements UserVerificationService {
    private static final Logger log = LoggerFactory.getLogger(UserVerificationServiceImpl.class);

    private final OtpService otpService;

    private final EmailService emailService;

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    public void sendVerification(User user) {

        // Send mobile OTP
        if (user.getMobileNumber() != null
                && !user.getMobileNumber().isBlank()) {

            otpService.sendOtp(
                    user.getMobileNumber(),
                    OtpPurpose.MOBILE_VERIFICATION
            );
        }

        // Create email verification token
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken =
                new EmailVerificationToken();

        verificationToken.setToken(token);

        verificationToken.setUser(user);

        verificationToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(24)
        );

        verificationToken.setVerified(false);

        emailVerificationTokenRepository.save(
                verificationToken
        );

        // Send verification email
        try {

            emailService.sendVerificationEmail(
                    user.getEmail(),
                    token
            );

        } catch (Exception e) {
            log.error("Verification email failed for {}", user.getEmail(), e);
        }
    }

    
    


    @Override
    public void verifyEmail(String token) {

        // Find verification token
        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invalid verification token."
                                )
                        );

        // Check token status
        if (Boolean.TRUE.equals(
                verificationToken.getVerified()
        )) {

            throw new BusinessException(
                    "Email is already verified."
            );
        }

        // Check token expiry
        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new BusinessException(
                    "Verification token has expired."
            );
        }

        // Get user
        User user =
                verificationToken.getUser();

        // Verify email
        user.setEmailVerified(true);

        // Activate user when both verifications are complete
        if (Boolean.TRUE.equals(
                user.getMobileVerified()
        )) {

            user.setStatus(
                    UserStatus.ACTIVE
            );
        }

        // Mark token as verified
        verificationToken.setVerified(true);

        verificationToken.setVerifiedDate(
                LocalDateTime.now()
        );

        // Save verification token
        emailVerificationTokenRepository.save(
                verificationToken
        );
    }


}