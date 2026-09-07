package com.sms.security.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(
        name = "VerifyForgotPasswordOtpResponseDto",
        description = "Response returned after successful OTP verification."
)
public class VerifyForgotPasswordOtpResponseDto {

    @Schema(
            description = "Password reset token",
            example = "c92d29aa-0b1d-4d18-89c5-8e1d1c3a8b56"
    )
    private String token;

    @Schema(
            description = "Response message",
            example = "OTP verified successfully."
    )
    private String message;

    @Schema(
            description = "Response timestamp"
    )
    private LocalDateTime timestamp;

}