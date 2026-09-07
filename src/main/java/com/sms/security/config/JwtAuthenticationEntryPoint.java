package com.sms.security.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;

	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {

		this.objectMapper = objectMapper;

	}

	@Override
	public void commence(

			HttpServletRequest request,

			HttpServletResponse response,

			AuthenticationException authException

	) throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("application/json");

		String message = """
				{
				    "success": false,
				    "message": "Unauthorized: Invalid or missing JWT Token",
				    "status": 401
				}
				""";

		response.getWriter().write(message);

	}

}