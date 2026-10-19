
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
     * Skip JWT processing for public email verification links.
     */
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        return HttpMethod.GET.matches(request.getMethod())
                && "/api/auth/verify-email".equals(
                        request.getServletPath());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // -------------------------------------------------
        // 1. Read Authorization header
        // -------------------------------------------------

        String authHeader = request.getHeader("Authorization");

        /*
         * No Bearer token:
         * Continue the filter chain without JWT processing.
         *
         * IMPORTANT: This call is outside the JWT try-catch
         * so controller/service exceptions are not swallowed.
         */
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // -------------------------------------------------
        // 2. Extract token
        // -------------------------------------------------

        String token = authHeader.substring(7).trim();

        if (token.isEmpty()) {

            log.warn("Empty Bearer token received for {} {}",
                    request.getMethod(),
                    request.getRequestURI());

            SecurityResponseWriter.sendError(
                    response,
                    "Invalid authentication token");

            return;
        }

        // -------------------------------------------------
        // 3. Validate JWT and establish authentication
        // -------------------------------------------------

        try {

            // Check token blacklist
            if (jwtBlacklistService.isBlacklisted(token)) {

                log.warn("Blacklisted JWT token rejected");

                SecurityResponseWriter.sendError(
                        response,
                        "Token expired or logged out");

                return;
            }

            // Extract username from JWT
            String username = jwtService.extractUsername(token);

            if (username == null || username.isBlank()) {

                log.warn("JWT does not contain a valid username");

                SecurityResponseWriter.sendError(
                        response,
                        "Invalid authentication token");

                return;
            }

            // Avoid replacing an existing authentication
            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                // Load user details
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Validate token against the loaded user
                if (!jwtService.isTokenValid(
                        token,
                        userDetails.getUsername())) {

                    log.warn("Invalid or expired JWT rejected");

                    SecurityResponseWriter.sendError(
                            response,
                            "Invalid or expired token");

                    return;
                }

                // Create authenticated principal
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Set authentication in Spring Security context
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                log.debug("JWT authentication successful");
            }

        } catch (JwtException e) {

            // JWT parsing, signature or expiration failure
            log.warn("JWT validation failed: {}",
                    e.getClass().getSimpleName());

            SecurityContextHolder.clearContext();

            SecurityResponseWriter.sendError(
                    response,
                    "Invalid or expired token");

            return;

        } catch (Exception e) {

            /*
             * Errors within JWT processing only.
             * The downstream filter chain is not called here,
             * so controller/service exceptions cannot be
             * mistaken for JWT processing errors.
             */
            log.error("JWT authentication processing failed. "
                            + "Request: {} {}. Exception: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    e.getClass().getSimpleName(),
                    e);

            SecurityContextHolder.clearContext();

            SecurityResponseWriter.sendError(
                    response,
                    "Authentication failed");

            return;
        }

        // -------------------------------------------------
        // 4. Continue the filter chain OUTSIDE the try-catch
        // -------------------------------------------------

        filterChain.doFilter(request, response);
    }
}
