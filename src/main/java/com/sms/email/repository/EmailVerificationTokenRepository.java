package com.sms.email.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sms.email.entity.EmailVerificationToken;

import jakarta.transaction.Transactional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
            delete from EmailVerificationToken e
            where e.user.id = :userId
            """)
    void deleteByUserId(Long userId);

}