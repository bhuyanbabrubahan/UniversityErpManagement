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
        name = "RefreshTokenResponse",
        description = "Response returned after generating a new access token using a valid refresh token."
)
public class RefreshTokenResponseDto {

    @Schema(
            description = "New JWT Access Token",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String accessToken;
    
    
    @Schema(
            description = "New Refresh Token",
            example = "3d7e0d51-72e8-45e2-a0d8-70db2a15c3d1"
    )
    private String refreshToken;
    

    @Builder.Default
    @Schema(
            description = "Token type",
            example = "Bearer"
    )
    private String tokenType = "Bearer";

    @Schema(
            description = "Access token expiry in seconds",
            example = "900"
    )
    private Long expiresIn;

    @Schema(
            description = "Email of authenticated user",
            example = "amit.sharma@ums.edu.in"
    )
    private String email;

    @Schema(
            description = "User role",
            example = "ADMIN"
    )
    private String role;

    @Schema(
            description = "Response generated time"
    )
    private LocalDateTime timestamp;

}