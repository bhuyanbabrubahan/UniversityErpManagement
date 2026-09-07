package com.sms.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.enums.RecordStatus;
import com.sms.security.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByTokenAndStatus(
	        String token,
	        RecordStatus status
	);


    void deleteByUserId(Long userId);

}