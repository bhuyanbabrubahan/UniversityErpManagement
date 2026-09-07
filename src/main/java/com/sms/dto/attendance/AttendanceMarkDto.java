package com.sms.dto.attendance;


import com.sms.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceMarkDto {

    @NotNull(message = "Student id is required.")
    private Long studentId;

    @NotNull(message = "Attendance status is required.")
    private AttendanceStatus attendanceStatus;

    @Size(
        max = 300,
        message = "Remarks cannot exceed 300 characters."
    )
    private String remarks;
}

