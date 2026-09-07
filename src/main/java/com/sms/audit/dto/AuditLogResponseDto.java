package com.sms.audit.dto;

import java.time.LocalDateTime;

import com.sms.audit.enums.AuditAction;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        name = "AuditLogResponse",
        description = "Response DTO containing audit log details."
)
public class AuditLogResponseDto {

    @Schema(
            description = "Audit Log ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Action performed",
            example = "CREATE"
    )
    private AuditAction action;

    @Schema(
            description = "Module name",
            example = "Student"
    )
    private String moduleName;

    @Schema(
            description = "Action description",
            example = "Student created successfully."
    )
    private String description;

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
            description = "IP Address",
            example = "192.168.1.10"
    )
    private String ipAddress;

    @Schema(
            description = "HTTP Request Method",
            example = "POST"
    )
    private String requestMethod;

    @Schema(
            description = "Request URL",
            example = "/api/students"
    )
    private String requestUrl;

    @Schema(
            description = "Action Timestamp"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Record Status",
            example = "ACTIVE"
    )
    private RecordStatus status;

}