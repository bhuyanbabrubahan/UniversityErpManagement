package com.sms.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    // Secret key used for signing and verifying JWT tokens.
    private String secret;

    // Access token expiration time in milliseconds.
    private long accessTokenExpiration;

    // Refresh token expiration time in milliseconds.
    private long refreshTokenExpiration;
}
