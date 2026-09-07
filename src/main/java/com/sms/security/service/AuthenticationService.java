package com.sms.security.service;

import com.sms.security.dto.ForgotPasswordRequestDto;
import com.sms.security.dto.LoginRequestDto;
import com.sms.security.dto.LoginResponseDto;
import com.sms.security.dto.LogoutResponseDto;
import com.sms.security.dto.PasswordResetResponseDto;
import com.sms.security.dto.RefreshTokenRequestDto;
import com.sms.security.dto.RefreshTokenResponseDto;
import com.sms.security.dto.UserRequestDto;
import com.sms.security.dto.UserResponseDto;
import com.sms.security.dto.ResetPasswordRequestDto;
import com.sms.security.dto.VerifyForgotPasswordOtpRequestDto;
import com.sms.security.dto.VerifyForgotPasswordOtpResponseDto;

public interface AuthenticationService {

	UserResponseDto register(UserRequestDto request);

	LoginResponseDto login(LoginRequestDto request);

	PasswordResetResponseDto forgotPassword(ForgotPasswordRequestDto request);

	PasswordResetResponseDto resetPassword(ResetPasswordRequestDto request);

	LogoutResponseDto logout(String token);

	RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request);

	String sendMobileVerificationOtp(String mobileNumber);

	String verifyMobileOtp(String mobileNumber, String otp);
	
	
	// VERIFY FORGOT PASSWORD OTP
	VerifyForgotPasswordOtpResponseDto verifyForgotPasswordOtp(
	        VerifyForgotPasswordOtpRequestDto request
	);

	Object getVerificationStatus(String email);
}