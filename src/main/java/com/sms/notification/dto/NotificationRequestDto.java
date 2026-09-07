package com.sms.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {

    @NotNull
    private Channel channel;

    @NotBlank
    private String recipient;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    public enum Channel {
        EMAIL,
        SMS
    }
}