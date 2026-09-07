package com.sms.audit.dto;

import java.time.LocalDateTime;

import com.sms.audit.enums.AuditAction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "AuditLogSearchRequest",
        description = "Search filters for audit logs."
)
public class AuditLogSearchRequestDto {

    @Schema(
            description = "Audit Action",
            example = "CREATE"
    )
    private AuditAction action;

    @Schema(
            description = "Module Name",
            example = "Student"
    )
    private String moduleName;

    @Schema(
            description = "Performed By",
            example = "admin@gmail.com"
    )
    private String performedBy;

    @Schema(
            description = "Role",
            example = "ADMIN"
    )
    private String role;

    @Schema(
            description = "Request Method",
            example = "POST"
    )
    private String requestMethod;

    @Schema(
            description = "Request URL",
            example = "/api/students"
    )
    private String requestUrl;

    @Schema(
            description = "From Timestamp",
            example = "2026-01-01T00:00:00"
    )
    private LocalDateTime fromDate;

    @Schema(
            description = "To Timestamp",
            example = "2026-12-31T23:59:59"
    )
    private LocalDateTime toDate;

}