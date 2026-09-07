package com.sms.util;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sms.payload.ApiResponseDto;

import jakarta.servlet.http.HttpServletResponse;

public final class SecurityResponseWriter {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	private SecurityResponseWriter() {
	}

	public static void sendError(HttpServletResponse response, String message) throws IOException {

		ApiResponseDto<Object> apiResponse = new ApiResponseDto<>();

		apiResponse.setSuccess(false);
		apiResponse.setMessage(message);
		apiResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		apiResponse.setData(null);
		apiResponse.setTimestamp(LocalDateTime.now());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		OBJECT_MAPPER.writeValue(response.getWriter(), apiResponse);
	}
}