package com.sms.dto.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceClassResponseDto {

    private Long attendanceSessionId;
    private AttendanceSessionStatus sessionStatus;
    private AttendanceSource attendanceSource;

    private Long facultySubjectMappingId;
    private Long facultyId;
    private String facultyName;

    private Long subjectId;
    private String subjectCode;
    private String subjectName;

    private Long departmentId;
    private String departmentName;

    private Long semesterId;
    private String semesterName;

    private LocalDate attendanceDate;
    private LocalDateTime attendanceOpenedAt;

    private List<AttendanceStudentDto> students;
}
