package com.sms.email.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.audit.annotation.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.email.entity.EmailVerificationToken;
import com.sms.email.repository.EmailVerificationTokenRepository;
import com.sms.email.service.EmailService;
import com.sms.email.service.EmailVerificationService;
import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.security.entity.User;
import com.sms.security.enums.UserStatus;
import com.sms.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

	private final EmailVerificationTokenRepository emailVerificationTokenRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.UPDATE,
	        module = "EMAIL_VERIFICATION",
	        description = "Verify email"
	)
    public String verifyEmail(String token) {


        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new BusinessException(
                                "Invalid verification token"));


        if(Boolean.TRUE.equals(
                verificationToken.getVerified())) {

            throw new BusinessException(
                    "Email already verified.");

        }


        if(verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new BusinessException(
                    "Verification link has expired.");

        }


        User user = verificationToken.getUser();


        // Email verify
        user.setEmailVerified(true);



     // Email + Mobile dono verify hone par hi account ACTIVE hoga

        if(Boolean.TRUE.equals(user.getEmailVerified())
                &&
           Boolean.TRUE.equals(user.getMobileVerified())) {


            user.setStatus(
                    UserStatus.ACTIVE
            );

        }
        else {

            user.setStatus(
                    UserStatus.PENDING_VERIFICATION
            );

        }


        verificationToken.setVerified(true);

        verificationToken.setVerifiedDate(
                LocalDateTime.now()
        );


        userRepository.save(user);


        emailVerificationTokenRepository.save(
                verificationToken
        );

        return "Email verified successfully.";

    }
    
    
    
	@Override
	@Transactional
	@AuditLog(
	        action = AuditAction.CREATE,
	        module = "EMAIL_VERIFICATION",
	        description = "Resend verification email"
	)
    public String resendVerification(String email) {


        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));


        // Email already verified check
        if(Boolean.TRUE.equals(user.getEmailVerified())) {

            throw new BusinessException(
                    "Email is already verified."
            );
        }


        // Purana token delete
        emailVerificationTokenRepository
                .deleteByUserId(user.getId());


        // Naya token create
        EmailVerificationToken verificationToken =
                new EmailVerificationToken();


        verificationToken.setToken(
                UUID.randomUUID().toString()
        );


        verificationToken.setUser(user);


        verificationToken.setExpiryDate(
                LocalDateTime.now().plusHours(24)
        );


        verificationToken.setVerified(false);


        emailVerificationTokenRepository.save(
                verificationToken
        );


        // Email send
        emailService.sendVerificationEmail(
                user.getEmail(),
                verificationToken.getToken()
        );


        return "Verification email sent successfully.";

    }
    
    
    
    
    
    
    
    

}