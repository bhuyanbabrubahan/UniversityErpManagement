package com.sms.dto.attendance;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkAttendanceResponseDto {

    private Long attendanceSessionId;
    private int totalStudents;
    private int presentCount;
    private int absentCount;
    private int halfDayCount;
    private int leaveCount;
    private int successCount;
    private int failedCount;
    private List<String> errors;
}
