package com.sms.dto.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.enums.AttendanceSessionStatus;
import com.sms.enums.AttendanceSource;
import com.sms.enums.AttendanceStatus;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AttendanceResponseDto")
public class AttendanceResponseDto {

    private Long id;

    private Long attendanceSessionId;
    private AttendanceSessionStatus sessionStatus;

    private Long studentId;
    private String rollNumber;
    private String studentName;

    private Long facultySubjectMappingId;
    private Long facultyId;
    private String facultyName;

    private Long subjectId;
    private String subjectCode;
    private String subjectName;

    private LocalDate attendanceDate;
    private AttendanceStatus attendanceStatus;
    private String remarks;
    private AttendanceSource attendanceSource;
    private LocalDateTime markedAt;

    private RecordStatus status;

    private Long createdByUserId;
    private String createdBy;
    private LocalDateTime createdAt;

    private Long updatedByUserId;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
