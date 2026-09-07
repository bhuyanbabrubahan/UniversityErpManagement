package com.sms.dto.attendance;

import com.sms.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceItemDto {

    @NotNull
    private Long studentId;

    @NotNull
    private AttendanceStatus attendanceStatus;

    @Size(max = 300)
    private String remarks;
}
