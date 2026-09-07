package com.sms.dto.attendance;

import java.time.LocalDate;

import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AttendanceSearchRequest", description = "Dynamic search filters for attendance records.")
public class AttendanceSearchRequest {

    private Long attendanceSessionId;
    private Long studentId;
    private Long facultyId;
    private Long subjectId;
    private Long facultySubjectMappingId;
    private LocalDate attendanceDate;
    private AttendanceStatus attendanceStatus;
    private AttendanceSessionStatus sessionStatus;
    private RecordStatus status;
}
