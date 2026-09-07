package com.sms.dto.attendance;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "BulkAttendanceRequestDto",
    description = "Request used to create or update attendance for multiple students in one attendance session."
)
public class BulkAttendanceRequestDto {

    @NotNull(message = "Attendance session id is required.")
    @Schema(description = "Attendance Session ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long attendanceSessionId;

    @NotEmpty(message = "Attendance records are required.")
    @Valid
    private List<BulkAttendanceItemDto> attendanceRecords;
}
