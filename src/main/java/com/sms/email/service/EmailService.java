package com.sms.email.service;


public interface EmailService {

    void sendVerificationEmail(
            String email,
            String token
    );

    void sendWelcomeEmail(
            String email,
            String fullName
    );

    void sendPasswordResetEmail(
            String email,
            String token
    );

    void sendPasswordChangedEmail(
            String email
    );

}