package com.sms.util;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sms.payload.ApiResponseDto;

public final class ResponseBuilder {

    private ResponseBuilder() {
        // Prevent object creation
    }


    // =========================================================
    // SUCCESS RESPONSE
    // =========================================================

    public static <T> ResponseEntity<ApiResponseDto<T>> success(
            T data,
            String message,
            HttpStatus status) {

        ApiResponseDto<T> response = new ApiResponseDto<>();

        response.setSuccess(true);
        response.setCode("SUCCESS");
        response.setMessage(message);
        response.setStatus(status.value());
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(null);

        return ResponseEntity
                .status(status)
                .body(response);
    }


    // =========================================================
    // ERROR RESPONSE - Existing / Backward Compatible
    // =========================================================

    public static <T> ResponseEntity<ApiResponseDto<T>> error(
            String message,
            HttpStatus status) {

        return error(
                "ERROR",
                message,
                status
        );
    }


    // =========================================================
    // ERROR RESPONSE - With Error Code
    // =========================================================

    public static <T> ResponseEntity<ApiResponseDto<T>> error(
            String code,
            String message,
            HttpStatus status) {

        ApiResponseDto<T> response = new ApiResponseDto<>();

        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setStatus(status.value());
        response.setData(null);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(null);

        return ResponseEntity
                .status(status)
                .body(response);
    }


    // =========================================================
    // ERROR RESPONSE - With Validation Errors
    // =========================================================

    public static <T> ResponseEntity<ApiResponseDto<T>> errorWithErrors(
            String code,
            String message,
            HttpStatus status,
            Map<String, String> errors) {

        ApiResponseDto<T> response = new ApiResponseDto<>();

        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setStatus(status.value());
        response.setData(null);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(errors);

        return ResponseEntity
                .status(status)
                .body(response);
    }


    // =========================================================
    // ERROR RESPONSE - With Data
    // =========================================================

    public static <T> ResponseEntity<ApiResponseDto<T>> errorWithData(
            String code,
            String message,
            HttpStatus status,
            T data) {

        ApiResponseDto<T> response = new ApiResponseDto<>();

        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setStatus(status.value());
        response.setData(data);
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(null);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}