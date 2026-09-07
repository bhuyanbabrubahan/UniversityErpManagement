package com.sms.security.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.security.entity.RefreshToken;
import com.sms.security.entity.User;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    /**
     * Find refresh token by token value.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find all active refresh tokens of a user.
     */
    List<RefreshToken> findByUserAndRevokedFalse(User user);

    /**
     * Delete all refresh tokens of a user.
     */
    void deleteByUser(User user);

    /**
     * Delete expired refresh tokens.
     * Used by Scheduler.
     */
    void deleteByExpiryDateBefore(LocalDateTime dateTime);
    
    

}