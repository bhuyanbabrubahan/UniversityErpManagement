package com.sms.dto.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceSource;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AttendanceSessionResponseDto")
public class AttendanceSessionResponseDto {

    private Long id;

    private Long facultySubjectMappingId;

    private Long facultyId;
    private String facultyName;

    private Long subjectId;
    private String subjectCode;
    private String subjectName;

    private Long semesterId;
    private String semesterName;

    private LocalDate attendanceDate;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private AttendanceSource attendanceSource;
    private AttendanceSessionStatus sessionStatus;
    private RecordStatus status;

    private int totalStudents;
    private int markedStudents;
    private int presentCount;
    private int absentCount;
    private int halfDayCount;
    private int leaveCount;
}
