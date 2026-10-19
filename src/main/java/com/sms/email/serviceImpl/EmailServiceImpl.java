package com.sms.email.serviceImpl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sms.email.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;


    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.reset-password-url}")
    private String resetPasswordUrl;


    // SMTP Debug purpose

    @Value("${spring.mail.host}")
    private String host;


    @Value("${spring.mail.port}")
    private String port;


    @Value("${spring.mail.username}")
    private String username;


    @Value("${spring.mail.password}")
    private String password;



    @Override
    public void sendPasswordResetEmail(
            String toEmail,
            String token) {


        log.info("========== PASSWORD RESET EMAIL ==========");


        log.info("Receiver Email : {}", toEmail);  //bad me hatana he

        log.info("Token Generated : {}", token); // bad me hatana he



        String resetLink = resetPasswordUrl + token;


        log.info("Reset Link : {}", resetLink);



        SimpleMailMessage message =
                new SimpleMailMessage();



        message.setFrom(fromEmail);

        message.setTo(toEmail);


        message.setSubject(
                "Password Reset Request - University Management System"
        );



        message.setText("""
                
                Hello,

                We received a request to reset your password.

                Please click the link below to reset your password:

                %s

                This link will expire in 15 minutes.

                If you did not request this password reset,
                please ignore this email.

                Regards,
                University Management System
                
                """.formatted(resetLink));



        try {


            log.info("Sending email through SMTP server...");


            mailSender.send(message);



            log.info(
                "Password reset email sent successfully to {}",
                toEmail
            );


        }
        catch (MailException exception) {


            log.error(
                "Email sending failed for {}",
                toEmail
            );


            log.error(
                "Mail Error : {}",
                exception.getMessage()
            );


            throw exception;
        }


        log.info("==========================================");
    }



    @Override
    public void sendVerificationEmail(String email, String token) {

        String verificationLink =
        		//"http://localhost:4200/verify-email?token=" + token;
        		"https://universityerpmanagement-production.up.railway.app/api/auth/verify-email?token=" + token;
        String subject = "Verify Your Email";

        String body = """
                Dear User,

                Thank you for registering.

                Please click the link below to verify your email.

                %s

                This link will expire in 24 hours.

                If you did not register, please ignore this email.

                Regards,
                Student Management System
                """.formatted(verificationLink);

        sendEmail(email, subject, body);
    }



    @Override
    public void sendWelcomeEmail(String email, String fullName) {

        String subject = "Welcome to Student Management System";

        String body = """
                Dear %s,

                Your email has been verified successfully.

                Welcome to Student Management System.

                You can now login using your registered email.

                Regards,
                Student Management System
                """.formatted(fullName);

        sendEmail(email, subject, body);
    }



    @Override
    public void sendPasswordChangedEmail(String email) {

        String subject = "Password Changed Successfully";

        String body = """
                Dear User,

                Your account password has been changed successfully.

                If you did not perform this action, please contact the administrator immediately.

                Regards,
                Student Management System
                """;

        sendEmail(email, subject, body);
    }

    
    //Helper method 
    private void sendEmail(
            String to,
            String subject,
            String body
    ) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient email must not be blank."
            );
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalStateException(
                    "MAIL_FROM is not configured."
            );
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail.trim());
        message.setTo(to.trim());
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);

            log.info(
                    "Email sent successfully. Type: {}, recipient: {}",
                    subject,
                    to
            );

        } catch (MailException exception) {
            log.error(
                    "Email delivery failed. Type: {}, recipient: {}",
                    subject,
                    to,
                    exception
            );

            throw exception;
        }
    }
}