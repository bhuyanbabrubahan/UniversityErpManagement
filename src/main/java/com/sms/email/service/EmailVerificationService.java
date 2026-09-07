package com.sms.email.service;

public interface EmailVerificationService {

	String verifyEmail(String token);

	String resendVerification(String email);

}