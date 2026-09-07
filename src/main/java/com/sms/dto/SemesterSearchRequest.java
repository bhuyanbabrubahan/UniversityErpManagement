package com.sms.dto;

import java.time.LocalDate;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "Semester Search Request",
        description = "Search criteria for filtering semesters"
)
public class SemesterSearchRequest {

    // ==================================================
    // Semester
    // ==================================================

    @Schema(
            description = "Semester ID",
            example = "1"
    )
    private Long semesterId;

    @Schema(
            description = "Semester name (partial search supported)",
            example = "Semester I"
    )
    private String semesterName;

    @Schema(
            description = "Semester number",
            example = "1"
    )
    private Integer semesterNumber;

    // ==================================================
    // Course
    // ==================================================

    @Schema(
            description = "Course ID",
            example = "2"
    )
    private Long courseId;

    @Schema(
            description = "Course code",
            example = "ME101"
    )
    private String courseCode;

    @Schema(
            description = "Course name (partial search supported)",
            example = "Mechanical Engineering"
    )
    private String courseName;

    // ==================================================
    // Semester Start Date Range
    // ==================================================

    @Schema(
            description = "Semester start date from",
            example = "2026-07-01"
    )
    private LocalDate startDateFrom;

    @Schema(
            description = "Semester start date to",
            example = "2026-12-31"
    )
    private LocalDate startDateTo;

    // ==================================================
    // Semester End Date Range
    // ==================================================

    @Schema(
            description = "Semester end date from",
            example = "2026-12-01"
    )
    private LocalDate endDateFrom;

    @Schema(
            description = "Semester end date to",
            example = "2027-01-15"
    )
    private LocalDate endDateTo;

    // ==================================================
    // Working Days Range
    // ==================================================

    @Schema(
            description = "Minimum working days",
            example = "100"
    )
    private Integer minWorkingDays;

    @Schema(
            description = "Maximum working days",
            example = "120"
    )
    private Integer maxWorkingDays;

    // ==================================================
    // Record Status
    // ==================================================

    @Schema(
            description = "Record status",
            example = "ACTIVE"
    )
    private RecordStatus status;

}