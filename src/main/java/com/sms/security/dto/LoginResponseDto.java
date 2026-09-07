package com.sms.security.dto;

import com.sms.security.enums.UserStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(
        name = "LoginResponseDto",
        description = "Response DTO returned after successful authentication."
)
public class LoginResponseDto {


    @Schema(
            description = "JWT Access Token",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    private String accessToken;



    @Schema(
            description = "JWT Refresh Token",
            example = "7e8c6a3b-8b2e-4d2a-9a11-8f0d2f"
    )
    private String refreshToken;



    @Schema(
            description = "Token type",
            example = "Bearer"
    )
    private String tokenType;



    @Schema(
            description = "Access token expiry time in seconds",
            example = "3600"
    )
    private long expiresIn;



    @Schema(
            description = "Logged in user email",
            example = "student@gmail.com"
    )
    private String email;
    
    @Schema(
            description = "Logged in user mobile",
            example = "9439292722"
    )
    private String mobileNumber;

    @Schema(
            description = "User role",
            example = "STUDENT"
    )
    private String role;
    
    private UserStatus status;
    
    @Schema(
            description = "Email verification status",
            example = "true"
    )
    private boolean emailVerified;



    @Schema(
            description = "Mobile verification status",
            example = "true"
    )
    private boolean mobileVerified;

}