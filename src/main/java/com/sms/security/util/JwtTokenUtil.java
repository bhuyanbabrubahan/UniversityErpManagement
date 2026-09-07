package com.sms.security.util;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil {

	public String getTokenFromRequest(HttpServletRequest request) {

        String authHeader =
                request.getHeader("Authorization");

        if(authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);

        }

        return null;

    }


}