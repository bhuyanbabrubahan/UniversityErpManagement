package com.sms.otp.service;

import com.sms.security.entity.PasswordResetToken;
import com.sms.security.entity.User;

public interface PasswordResetTokenService {

	String createToken(User user);
	
	PasswordResetToken findByToken(String token);

    void validateToken(String token);

	User getUserByToken(String token);

	void markTokenUsed(String token);

}