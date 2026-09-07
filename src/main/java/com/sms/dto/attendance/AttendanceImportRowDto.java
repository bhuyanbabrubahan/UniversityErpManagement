package com.sms.dto.attendance;


import java.time.LocalDate;

import com.sms.enums.AttendanceStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceImportRowDto {

    private String rollNumber;

    private String studentCode;

    private LocalDate attendanceDate;

    private AttendanceStatus attendanceStatus;

    private String remarks;
}
