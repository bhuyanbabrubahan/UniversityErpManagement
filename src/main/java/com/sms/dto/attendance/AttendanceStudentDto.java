package com.sms.dto.attendance;


import com.sms.enums.AttendanceStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AttendanceStudentDto")
public class AttendanceStudentDto {

    @Schema(example = "101")
    private Long studentId;

    @Schema(example = "BCA2026001")
    private String studentCode;

    @Schema(example = "01")
    private String rollNumber;

    @Schema(example = "Rahul Kumar")
    private String studentName;

    @Schema(example = "Computer Science")
    private String departmentName;

    @Schema(example = "BCA")
    private String courseName;

    @Schema(example = "3rd Semester")
    private String semesterName;

    /**
     * Existing attendance for selected date.
     * Null means attendance is not marked yet.
     */
    @Schema(example = "PRESENT", nullable = true)
    private AttendanceStatus attendanceStatus;

    @Schema(example = "Already marked")
    private String remarks;
}

