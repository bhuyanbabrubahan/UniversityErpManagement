package com.sms.security.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.sms.exception.BusinessException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.security.entity.RefreshToken;
import com.sms.security.entity.User;
import com.sms.security.repository.RefreshTokenRepository;
import com.sms.security.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final Logger log =
            LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    // Database-backed refresh token expiry follows the same property as JWT config.
    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpirationMillis;

    // CREATE REFRESH TOKEN
    @SuppressWarnings("null")
	@Override
    public RefreshToken createRefreshToken(User user) {

        // Remove previous active refresh token
        refreshTokenRepository.deleteByUser(user);

        LocalDateTime now = LocalDateTime.now();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(now.plusNanos(refreshTokenExpirationMillis * 1_000_000L))
                .revoked(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // FIND REFRESH TOKEN
    @Override
    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refresh token not found"
                        )
                );
    }

    // VERIFY REFRESH TOKEN
    @Override
    public RefreshToken verifyExpiration(
            RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new BusinessException(
                    "Refresh token expired. Please login again."
            );
        }

        if (refreshToken.isRevoked()) {

            throw new BusinessException(
                    "Refresh token revoked."
            );
        }

        return refreshToken;
    }

    // DELETE USER REFRESH TOKENS
    @Override
    public void deleteByUser(User user) {

        refreshTokenRepository.deleteByUser(user);
    }

    // CHECK REFRESH TOKEN EXPIRATION
    @Override
    public boolean isExpired(
            RefreshToken refreshToken) {

        boolean expired = LocalDateTime.now()
                .isAfter(refreshToken.getExpiryDate());

        if (expired) {

            log.warn(
                    "Refresh token expired for user: {}",
                    refreshToken.getUser().getEmail()
            );
        }

        return expired;
    }
}
