package com.sms.dto;

import java.time.LocalDate;

import com.sms.enums.ExamType;
import com.sms.enums.RecordStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "ExamSearchRequest",
    description = "Request DTO used to search examination records using optional search filters."
)
public class ExamSearchRequest {

    @Schema(
        description = "Search by Semester ID",
        example = "1"
    )
    private Long semesterId;

    @Schema(
        description = "Search by Subject ID",
        example = "5"
    )
    private Long subjectId;

    @Schema(
        description = "Search by Exam Type",
        example = "THEORY",
        allowableValues = {
            "THEORY",
            "PRACTICAL",
            "VIVA"
        }
    )
    private ExamType examType;

    @Schema(
        description = "Search by Exam Date",
        example = "2026-12-15"
    )
    private LocalDate examDate;

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