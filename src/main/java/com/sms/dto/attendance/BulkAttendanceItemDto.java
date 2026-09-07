package com.sms.dto.attendance;


import com.sms.enums.AttendanceStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkAttendanceItemDto {

    @Schema(
        description = "Unique student ID",
        example = "101",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Student id is required.")
    private Long studentId;

    @Schema(
        description = "Attendance status",
        example = "PRESENT",
        allowableValues = {
            "PRESENT",
            "ABSENT",
            "HALF_DAY",
            "LEAVE"
        },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Attendance status is required.")
    private AttendanceStatus attendanceStatus;

    @Schema(
        description = "Optional attendance remarks",
        example = "Medical leave"
    )
    @Size(
        max = 300,
        message = "Remarks cannot exceed 300 characters."
    )
    private String remarks;
}

