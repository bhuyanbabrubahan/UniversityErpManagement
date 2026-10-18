package com.sms.security.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // ============================================================
    // JWT CONFIGURATION
    // ============================================================

    private static final String TOKEN_TYPE_CLAIM = "tokenType";

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";

    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    /*
     * Keep token lifetimes configurable so local and Railway deployments
     * use the same explicit properties instead of hidden hard-coded values.
     */
    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;


    /*
     * IMPORTANT:
     *
     * Secret should NOT be hardcoded in source code.
     *
     * Configure it through application.properties / environment
     * variable.
     */
    private final String secretKey;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public JwtService(
            @Value("${jwt.secret}") String secretKey) {

        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException(
                    "JWT secret key must not be empty"
            );
        }

        this.secretKey = secretKey;
    }


    // ============================================================
    // GENERATE ACCESS TOKEN
    // ============================================================

    public String generateToken(
            UserDetails userDetails) {

        return Jwts.builder()

                .subject(userDetails.getUsername())

                .claim(
                        TOKEN_TYPE_CLAIM,
                        ACCESS_TOKEN_TYPE
                )

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessTokenExpiration
                        )
                )

                .signWith(getSignKey())

                .compact();
    }


    // ============================================================
    // GENERATE REFRESH TOKEN
    // ============================================================

    public String generateRefreshToken(
            UserDetails userDetails) {

        return Jwts.builder()

                .subject(userDetails.getUsername())

                .claim(
                        TOKEN_TYPE_CLAIM,
                        REFRESH_TOKEN_TYPE
                )

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + refreshTokenExpiration
                        )
                )

                .signWith(getSignKey())

                .compact();
    }


    // ============================================================
    // ACCESS TOKEN EXPIRATION
    // ============================================================

    public long getAccessTokenExpirationInSeconds() {

        return accessTokenExpiration / 1000;
    }


    // ============================================================
    // REFRESH TOKEN EXPIRATION
    // ============================================================

    public long getRefreshTokenExpirationInSeconds() {

        return refreshTokenExpiration / 1000;
    }


    // ============================================================
    // EXTRACT USERNAME
    // ============================================================

    public String extractUsername(
            String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }


    // ============================================================
    // EXTRACT TOKEN TYPE
    // ============================================================

    public String extractTokenType(
            String token) {

        return extractClaim(
                token,
                claims -> claims.get(
                        TOKEN_TYPE_CLAIM,
                        String.class
                )
        );
    }


    // ============================================================
    // CHECK ACCESS TOKEN
    // ============================================================

    public boolean isAccessToken(
            String token) {

        return ACCESS_TOKEN_TYPE.equals(
                extractTokenType(token)
        );
    }


    // ============================================================
    // CHECK REFRESH TOKEN
    // ============================================================

    public boolean isRefreshToken(
            String token) {

        return REFRESH_TOKEN_TYPE.equals(
                extractTokenType(token)
        );
    }


    // ============================================================
    // EXTRACT CLAIM
    // ============================================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }


    // ============================================================
    // EXTRACT ALL CLAIMS
    // ============================================================

    private Claims extractAllClaims(
            String token) {

        if (!StringUtils.hasText(token)) {
            throw new JwtException(
                    "JWT token must not be empty"
            );
        }

        Jws<Claims> parsedToken = Jwts.parser()

                .verifyWith(getSignKey())

                .build()

                .parseSignedClaims(token);

        return parsedToken.getPayload();
    }


    // ============================================================
    // VALIDATE ACCESS TOKEN
    // ============================================================

    public boolean isTokenValid(
            String token,
            String username) {

        if (!StringUtils.hasText(token)
                || !StringUtils.hasText(username)) {

            return false;
        }

        try {

            String tokenUsername =
                    extractUsername(token);

            return username.equals(tokenUsername)
                    && isAccessToken(token);

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }


    // ============================================================
    // VALIDATE REFRESH TOKEN
    // ============================================================

    public boolean isRefreshTokenValid(
            String token,
            String username) {

        if (!StringUtils.hasText(token)
                || !StringUtils.hasText(username)) {

            return false;
        }

        try {

            String tokenUsername =
                    extractUsername(token);

            return username.equals(tokenUsername)
                    && isRefreshToken(token);

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }


    // ============================================================
    // CHECK TOKEN EXPIRATION
    // ============================================================

    public boolean isTokenExpired(
            String token) {

        try {

            Date expiration =
                    extractClaim(
                            token,
                            Claims::getExpiration
                    );

            return expiration == null
                    || expiration.before(new Date());

        } catch (JwtException | IllegalArgumentException e) {

            return true;
        }
    }


    // ============================================================
    // SIGNING KEY
    // ============================================================

    private SecretKey getSignKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }
}