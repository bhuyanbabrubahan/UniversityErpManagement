package com.sms.otp.service;

public interface SmsService {

	void sendOtp(String mobileNumber, String otp);

}