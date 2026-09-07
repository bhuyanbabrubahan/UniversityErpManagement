package com.sms.dto.attendance;


import com.sms.enums.AttendanceStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceStudentRowDto {

    private Long studentId;

    private String rollNumber;

    private String studentName;

    private String departmentName;

    private String courseName;

    private String section;

    private String batch;

    private Long attendanceId;

    private AttendanceStatus attendanceStatus;

    private String remarks;
}

