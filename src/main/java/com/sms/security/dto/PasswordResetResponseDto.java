package com.sms.security.dto;


import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "PasswordResetResponseDto",
        description = "Response DTO for password reset operations."
)
public class PasswordResetResponseDto {


    @Schema(
            description = "Response message",
            example = "Password reset successfully."
    )
    private String message;


    @Schema(
            description = "Response timestamp"
    )
    private LocalDateTime timestamp;

}