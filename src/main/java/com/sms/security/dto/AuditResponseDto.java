package com.sms.security.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditResponseDto {

    private Long createdByUserId;
    private String createdBy;
    private LocalDateTime createdDate;

    private Long updatedByUserId;
    private String updatedBy;
    private LocalDateTime updatedDate;
}