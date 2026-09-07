package com.sms.security.service;

public interface JwtBlacklistService {

	void blacklistToken(String token);

	boolean isBlacklisted(String token);

}