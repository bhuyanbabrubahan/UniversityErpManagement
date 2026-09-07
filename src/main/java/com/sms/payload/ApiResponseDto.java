package com.sms.payload;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

    private boolean success;

    private String code;

    private String message;

    private int status;

    private T data;

    private LocalDateTime timestamp;

    private Map<String, String> errors;
}