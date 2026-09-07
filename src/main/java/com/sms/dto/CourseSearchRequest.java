package com.sms.dto;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSearchRequest {

    @Schema(
            description = "Course Name",
            example = "Java"
    )
    private String courseName;

    @Schema(
            description = "Course Code",
            example = "CSE101"
    )
    private String courseCode;

    @Schema(
            description = "Department Id",
            example = "2"
    )
    private Long departmentId;

    @Schema(
            description = "Record Status",
            example = "ACTIVE"
    )
    private RecordStatus status;


    // ==========================
    // Duration Filter
    // ==========================

    @Schema(
            description = "Minimum Duration (Months)",
            example = "12"
    )
    private Integer minDuration;

    @Schema(
            description = "Maximum Duration (Months)",
            example = "48"
    )
    private Integer maxDuration;

}