package com.sms.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
    name = "FacultySubjectRequestDto",
    description = "Request DTO used to assign a faculty member to a subject for a specific academic year."
)
public class FacultySubjectRequestDto {

    @Schema(
        description = "Unique Faculty ID",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Faculty id is required")
    private Long facultyId;

    @Schema(
        description = "Unique Subject ID",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Subject id is required")
    private Long subjectId;

    @Schema(
        description = "Date on which the subject is assigned to the faculty (yyyy-MM-dd)",
        example = "2026-07-15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Assigned date is required")
    @FutureOrPresent(message = "Assigned date cannot be past")
    private LocalDate assignedDate;

    @Schema(
        description = "Academic year for which the faculty is assigned to the subject",
        example = "2026-2027",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Academic year is required")
    private String academicYear;

}