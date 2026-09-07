package com.sms.dto.attendance;

import java.time.LocalDate;

import com.sms.enums.AttendanceSource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "AttendanceSessionRequestDto",
    description = "Request used to open an attendance session for a faculty subject mapping."
)
public class AttendanceSessionRequestDto {

    @NotNull(message = "Faculty subject mapping id is required.")
    @Schema(description = "Faculty Subject Mapping ID", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long facultySubjectMappingId;

    @NotNull(message = "Attendance date is required.")
    @Schema(description = "Attendance date", example = "2026-08-21", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate attendanceDate;

    @NotNull(message = "Attendance source is required.")
    @Schema(
        description = "Source from which the session is opened",
        example = "ONLINE",
        allowableValues = {"ONLINE", "EXCEL_IMPORT", "MANUAL_BACKDATE"},
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private AttendanceSource attendanceSource;
}
