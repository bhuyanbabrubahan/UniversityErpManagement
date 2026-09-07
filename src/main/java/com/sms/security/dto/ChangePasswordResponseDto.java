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
        name = "ChangePasswordResponseDto",
        description = "Response DTO returned after successful password change."
)
public class ChangePasswordResponseDto {


    @Schema(
            description = "Password change success message",
            example = "Password changed successfully."
    )
    private String message;


    @Schema(
            description = "Password changed timestamp",
            example = "2026-07-17T22:45:30"
    )
    private LocalDateTime changedAt;

}