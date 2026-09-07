package com.sms.security.service;

import com.sms.security.entity.RefreshToken;
import com.sms.security.entity.User;

public interface RefreshTokenService {

	/**
	 * Create and save refresh token for user.
	 */
	RefreshToken createRefreshToken(User user);

	/**
	 * Find refresh token from database.
	 */
	RefreshToken findByToken(String token);

	/**
	 * Validate refresh token expiry.
	 */
	RefreshToken verifyExpiration(RefreshToken refreshToken);

	/**
	 * Delete all refresh tokens of user.
	 */
	void deleteByUser(User user);
	

	boolean isExpired(RefreshToken refreshToken);

}