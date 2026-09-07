package com.sms.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.sms.enums.ExamType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "ExamRequestDto",
    description = "Request DTO used to create or update an examination."
)
public class ExamRequestDto {

    // ==========================
    // Semester
    // ==========================

    @Schema(
        description = "Semester ID in which the exam is conducted",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Semester id is required")
    private Long semesterId;

    // ==========================
    // Subject
    // ==========================

    @Schema(
        description = "Subject ID for which the exam is conducted",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Subject id is required")
    private Long subjectId;

    // ==========================
    // Exam Information
    // ==========================

    @Schema(
        description = "Exam date",
        example = "2026-12-15",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @Schema(
        description = "Exam start time",
        example = "10:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @Schema(
        description = "Exam end time",
        example = "13:00",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @Schema(
        description = "Exam name",
        example = "Mid Semester Examination",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Exam name is required")
    private String examName;

    @Schema(
        description = "Type of examination",
        example = "THEORY",
        allowableValues = {
            "THEORY",
            "PRACTICAL",
            "VIVA"
        },
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @Schema(
        description = "Academic year",
        example = "2026-2027",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Academic year is required")
    private String academicYear;

    // ==========================
    // Marks
    // ==========================

    @Schema(
        description = "Maximum marks for the examination",
        example = "100",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Maximum marks is required")
    @Positive(message = "Maximum marks must be greater than zero")
    private Integer maximumMarks;

    @Schema(
        description = "Minimum marks required to pass the examination",
        example = "40",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Passing marks is required")
    @PositiveOrZero(message = "Passing marks cannot be negative")
    private Integer passingMarks;

}