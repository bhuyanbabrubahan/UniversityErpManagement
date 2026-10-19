
package com.sms.email.serviceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sms.email.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.reset-password-url}")
    private String resetPasswordUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {

        String resetLink = resetPasswordUrl + token;

        String body = """
                Hello,

                We received a request to reset your password.

                Please use this link to reset your password:

                %s

                This link will expire in 15 minutes.

                If you did not request this password reset,
                please ignore this email.

                Regards,
                University Management System
                """.formatted(resetLink);

        sendEmail(
                toEmail,
                "Password Reset Request - University Management System",
                body
        );
    }

    @Override
    public void sendVerificationEmail(String email, String token) {

        String verificationLink =
                "https://universityerpmanagement-production.up.railway.app"
                + "/api/auth/verify-email?token=" + token;

        String body = """
                Dear User,

                Thank you for registering.

                Please click the link below to verify your email:

                %s

                This link will expire in 24 hours.

                Regards,
                Student Management System
                """.formatted(verificationLink);

        sendEmail(email, "Verify Your Email", body);
    }

    @Override
    public void sendWelcomeEmail(String email, String fullName) {

        String body = """
                Dear %s,

                Your email has been verified successfully.

                Welcome to Student Management System.

                You can now login using your registered email.

                Regards,
                Student Management System
                """.formatted(fullName);

        sendEmail(
                email,
                "Welcome to Student Management System",
                body
        );
    }

    @Override
    public void sendPasswordChangedEmail(String email) {

        String body = """
                Dear User,

                Your account password has been changed successfully.

                If you did not perform this action,
                please contact the administrator immediately.

                Regards,
                Student Management System
                """;

        sendEmail(email, "Password Changed Successfully", body);
    }

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

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "RESEND_API_KEY is not configured."
            );
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalStateException(
                    "MAIL_FROM is not configured."
            );
        }

        try {
            ObjectNode payload = objectMapper.createObjectNode();

            payload.put("from", fromEmail.trim());
            payload.put("subject", subject);
            payload.put("text", body);

            ArrayNode recipients = payload.putArray("to");
            recipients.add(to.trim());

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                // API key, email body aur reset token log nahi karte.
                log.error(
                        "Email API failed. HTTP status: {}, response: {}",
                        response.statusCode(),
                        response.body()
                );

                throw new IllegalStateException(
                        "Email delivery request failed with HTTP status "
                                + response.statusCode()
                );
            }

            log.info(
                    "Email API accepted request. Type: {}, HTTP status: {}",
                    subject,
                    response.statusCode()
            );

        } catch (IOException e) {

            log.error("Could not communicate with email API.", e);

            throw new IllegalStateException(
                    "Unable to communicate with email service.", e
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Email request was interrupted.", e
            );
        }
    }
}
