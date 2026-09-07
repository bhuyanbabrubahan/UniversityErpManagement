package com.sms.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sms.payload.ApiResponseDto;
import com.sms.util.ResponseBuilder;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // =========================================================
    // Duplicate Student
    // =========================================================

    @ExceptionHandler(DuplicateStudentException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleDuplicateStudentException(
            DuplicateStudentException ex) {

        log.warn("Duplicate student: {}", ex.getMessage());

        return ResponseBuilder.error(
                "DUPLICATE_STUDENT",
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }


    // =========================================================
    // Duplicate Department
    // =========================================================

    @ExceptionHandler(DuplicateDepartmentException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleDuplicateDepartmentException(
            DuplicateDepartmentException ex) {

        log.warn("Duplicate department: {}", ex.getMessage());

        return ResponseBuilder.error(
                "DUPLICATE_DEPARTMENT",
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }


    // =========================================================
    // Resource Not Found
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        log.warn("Resource not found: {}", ex.getMessage());

        return ResponseBuilder.error(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }


    // =========================================================
    // Validation Exception - @Valid
    // =========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseBuilder.errorWithErrors(
                "VALIDATION_FAILED",
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }


    // =========================================================
    // Business Exception
    // =========================================================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleBusinessException(
            BusinessException ex) {

        log.warn("Business rule violation: {}", ex.getMessage());

        return ResponseBuilder.error(
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }


    // =========================================================
    // Account Verification Exception
    // =========================================================

    @ExceptionHandler(AccountVerificationException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleAccountVerificationException(
            AccountVerificationException ex) {

        log.warn(
                "Account verification failed: {}",
                ex.getMessage()
        );

        return ResponseBuilder.errorWithData(
                "ACCOUNT_VERIFICATION_FAILED",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ex.getData()
        );
    }


    // =========================================================
    // Invalid JSON / Request Body
    // =========================================================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleJsonParseException(
            HttpMessageNotReadableException ex) {

        log.warn("Invalid request body received", ex);

        return ResponseBuilder.error(
                "INVALID_REQUEST_BODY",
                "Invalid request format or invalid field value.",
                HttpStatus.BAD_REQUEST
        );
    }


    // =========================================================
    // Database Constraint Violation
    // =========================================================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        log.error("Database constraint violation", ex);

        return ResponseBuilder.error(
                "DATA_INTEGRITY_VIOLATION",
                "Request could not be completed because it violates a data constraint.",
                HttpStatus.CONFLICT
        );
    }


    // =========================================================
    // Generic Exception - Final Fallback
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>>
    handleException(Exception ex) {

        // Detailed error is available only in server logs.
        // Internal exception details are not exposed to client.
        log.error("Unexpected error occurred", ex);

        return ResponseBuilder.error(
                "INTERNAL_SERVER_ERROR",
                "Something went wrong. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}