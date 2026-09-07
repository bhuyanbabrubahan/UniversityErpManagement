package com.sms.dto.attendance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "AttendanceClassRequestDto",
    description = "Request used to load students and existing attendance for an opened attendance session."
)
public class AttendanceClassRequestDto {

    @NotNull(message = "Attendance session id is required.")
    @Schema(description = "Attendance Session ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long attendanceSessionId;
}
