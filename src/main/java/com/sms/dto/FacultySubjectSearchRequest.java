package com.sms.dto;

import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "FacultySubjectSearchRequest",
    description = "Request DTO used to search faculty-subject mappings using optional search criteria."
)
public class FacultySubjectSearchRequest {

    @Schema(
        description = "Search by Faculty ID",
        example = "10"
    )
    private Long facultyId;

    @Schema(
        description = "Search by Subject ID",
        example = "5"
    )
    private Long subjectId;

    @Schema(
        description = "Search by Academic Year",
        example = "2026-2027"
    )
    private String academicYear;

    @Schema(
        description = "Search by Record Status. By default ACTIVE records are returned.",
        example = "ACTIVE",
        allowableValues = {
            "ACTIVE",
            "INACTIVE",
            "DELETED"
        }
    )
    private RecordStatus status;

}