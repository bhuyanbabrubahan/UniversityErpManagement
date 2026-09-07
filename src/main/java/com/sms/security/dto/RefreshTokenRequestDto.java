package com.sms.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "RefreshTokenRequest",
        description = "Request DTO used to generate a new access token."
)
public class RefreshTokenRequestDto {

    @NotBlank(message = "Refresh token is required")
    @Schema(
            description = "Refresh Token",
            example = "c84bbcb5-8c0d-4f69-b2e4-9d7b8e31ef11"
    )
    private String refreshToken;

}