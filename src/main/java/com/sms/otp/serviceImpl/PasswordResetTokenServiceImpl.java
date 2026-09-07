package com.sms.otp.serviceImpl;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.enums.RecordStatus;
import com.sms.exception.BusinessException;
import com.sms.otp.service.PasswordResetTokenService;
import com.sms.security.entity.PasswordResetToken;
import com.sms.security.entity.User;
import com.sms.security.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetTokenServiceImpl 
        implements PasswordResetTokenService {


    private final PasswordResetTokenRepository repository;



    private static final long TOKEN_VALIDITY_MINUTES = 10;



    // =====================================
    // CREATE RESET TOKEN
    // =====================================

    @Override
    public String createToken(User user) {


        /*
         * Remove old active reset tokens.
         *
         * Reason:
         * One user should have only one valid password reset session.
         */
        repository.deleteByUserId(
                user.getId()
        );



        String token =
                UUID.randomUUID()
                .toString();



        PasswordResetToken resetToken =
                PasswordResetToken.builder()

                .token(token)

                .user(user)

                .expiryDate(
                        LocalDateTime.now()
                        .plusMinutes(
                                TOKEN_VALIDITY_MINUTES
                        )
                )

                .used(false)

                .status(
                        RecordStatus.ACTIVE
                )

                .build();



        repository.save(resetToken);



        return token;
    }





    // =====================================
    // FIND TOKEN
    // =====================================

    @Override
    @Transactional(readOnly = true)
    public PasswordResetToken findByToken(
            String token
    ) {


        return repository
                .findByTokenAndStatus(
                        token,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Invalid reset token."
                        )
                );

    }





    // =====================================
    // VALIDATE TOKEN
    // =====================================

    @Override
    @Transactional(readOnly = true)
    public void validateToken(
            String token
    ) {


        PasswordResetToken resetToken =
                findByToken(token);



        if(resetToken.isUsed()) {


            throw new BusinessException(
                    "Reset token already used."
            );

        }



        if(resetToken.getExpiryDate()
                .isBefore(
                    LocalDateTime.now()
                )) {


            resetToken.setStatus(
                    RecordStatus.INACTIVE
            );


            repository.save(
                    resetToken
            );


            throw new BusinessException(
                    "Reset token expired."
            );

        }

    }





    // =====================================
    // GET USER FROM TOKEN
    // =====================================

    @Override
    @Transactional(readOnly = true)
    public User getUserByToken(
            String token
    ) {


        PasswordResetToken resetToken =
                findByToken(token);



        return resetToken.getUser();

    }





    // =====================================
    // MARK TOKEN USED
    // =====================================

    @Override
    public void markTokenUsed(
            String token
    ) {


        PasswordResetToken resetToken =
                findByToken(token);



        resetToken.setUsed(true);



        resetToken.setStatus(
                RecordStatus.INACTIVE
        );



        repository.save(
                resetToken
        );

    }

}