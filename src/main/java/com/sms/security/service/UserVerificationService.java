package com.sms.security.service;

import com.sms.security.entity.User;

public interface UserVerificationService {

    void sendVerification(User user);

    void verifyEmail(String token);
}