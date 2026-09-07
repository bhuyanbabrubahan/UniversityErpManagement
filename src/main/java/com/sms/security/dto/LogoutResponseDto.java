package com.sms.security.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "LogoutResponse",
    description = "Response returned after successful logout."
)
public class LogoutResponseDto {

    @Schema(
        description = "Logout message",
        example = "Logout successful."
    )
    private String message;

    @Schema(
        description = "Logout timestamp"
    )
    private LocalDateTime timestamp;

}