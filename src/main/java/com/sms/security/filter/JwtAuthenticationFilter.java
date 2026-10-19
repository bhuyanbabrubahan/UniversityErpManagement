package com.sms.security.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sms.security.service.CustomUserDetailsService;
import com.sms.security.service.JwtBlacklistService;
import com.sms.security.service.JwtService;
import com.sms.util.SecurityResponseWriter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    /**
     * Skip JWT authentication for the public email verification endpoint.
     *
     * This prevents an old or expired Bearer token from blocking
     * email verification requests.
     */
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        return HttpMethod.GET.matches(request.getMethod())
                && "/api/auth/verify-email".equals(
                        request.getServletPath()
                );
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {

            // ==========================================
            // 1. Read Authorization Header
            // ==========================================

            String authHeader = request.getHeader("Authorization");

            /*
             * No Authorization header or request is not
             * using the Bearer authentication scheme.
             *
             * Let Spring Security continue processing.
             */
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                filterChain.doFilter(request, response);
                return;
            }

            // ==========================================
            // 2. Extract JWT Token
            // ==========================================

            String token = authHeader.substring(7).trim();

            /*
             * Prevent processing an empty Bearer token.
             */
            if (token.isEmpty()) {

                log.warn("Empty JWT token received");

                SecurityResponseWriter.sendError(
                        response,
                        "Invalid authentication token"
                );

                return;
            }

            log.debug("JWT token received");

            // ==========================================
            // 3. Check Blacklisted Token
            // ==========================================

            if (jwtBlacklistService.isBlacklisted(token)) {

                log.warn("Blocked blacklisted JWT token");

                SecurityResponseWriter.sendError(
                        response,
                        "Token expired or logged out"
                );

                return;
            }

            // ==========================================
            // 4. Extract Username
            // ==========================================

            String username = jwtService.extractUsername(token);

            if (username == null || username.isBlank()) {

                log.warn("Username not found in JWT token");

                SecurityResponseWriter.sendError(
                        response,
                        "Invalid authentication token"
                );

                return;
            }

            log.debug("JWT username extracted: {}", username);

            // ==========================================
            // 5. Check Existing Authentication
            // ==========================================

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() != null) {

                filterChain.doFilter(request, response);
                return;
            }

            // ==========================================
            // 6. Load User Details
            // ==========================================

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // ==========================================
            // 7. Validate JWT
            // ==========================================

            if (!jwtService.isTokenValid(
                    token,
                    userDetails.getUsername())) {

                log.warn(
                        "Invalid JWT token for user: {}",
                        username
                );

                SecurityResponseWriter.sendError(
                        response,
                        "Invalid or expired token"
                );

                return;
            }

            // ==========================================
            // 8. Create Authentication
            // ==========================================

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            // ==========================================
            // 9. Set Authentication in SecurityContext
            // ==========================================

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            log.debug(
                    "JWT authentication successful for user: {}",
                    username
            );

        } catch (JwtException e) {

            /*
             * JWT parsing, signature or expiration related errors.
             *
             * Do not expose internal JWT exception details
             * to the client.
             */
            log.warn(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            SecurityResponseWriter.sendError(
                    response,
                    "Invalid or expired token"
            );

            return;

        } catch (Exception e) {

            /*
             * Unexpected server-side error.
             */
            log.error(
                    "Unexpected error during JWT authentication",
                    e
            );

            SecurityResponseWriter.sendError(
                    response,
                    "Authentication failed"
            );

            return;
        }

        // ==========================================
        // 10. Continue Filter Chain
        // ==========================================

        filterChain.doFilter(request, response);
    }
}