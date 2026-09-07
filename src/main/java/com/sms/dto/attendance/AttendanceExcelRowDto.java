package com.sms.dto.attendance;


import com.sms.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceExcelRowDto {

    private String studentCode;

    private String rollNumber;

    @NotNull(message = "Attendance status is required.")
    private AttendanceStatus attendanceStatus;

    @Size(max = 300)
    private String remarks;
}

