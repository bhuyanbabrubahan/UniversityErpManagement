package com.sms.dto;

import java.math.BigDecimal;

import com.sms.enums.RecordStatus;
import com.sms.enums.ResultStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
    name = "ResultSearchRequest",
    description = "Request DTO used to search student results using optional filters."
)
public class ResultSearchRequest {

    // ==========================
    // Student Filters
    // ==========================

    @Schema(
            description = "Search by Student ID",
            example = "1"
    )
    private Long studentId;

    @Schema(
            description = "Search by Student Roll Number",
            example = "STU20260001"
    )
    private String rollNumber;

    @Schema(
            description = "Search by Student Name (partial search supported)",
            example = "Rahul"
    )
    private String studentName;

    // ==========================
    // Exam Filter
    // ==========================

    @Schema(
            description = "Search by Exam ID",
            example = "5"
    )
    private Long examId;

    // ==========================
    // Subject Filter
    // ==========================

    @Schema(
            description = "Search by Subject ID",
            example = "3"
    )
    private Long subjectId;

    // ==========================
    // Semester Filter
    // ==========================

    @Schema(
            description = "Search by Semester ID",
            example = "2"
    )
    private Long semesterId;

    // ==========================
    // Result Filters
    // ==========================

    @Schema(
            description = "Search by Result Status",
            example = "PASS",
            allowableValues = {
                    "PASS",
                    "FAIL"
            }
    )
    private ResultStatus resultStatus;

    @Schema(
            description = "Search by Grade",
            example = "A"
    )
    private String grade;

    // ==========================
    // Percentage Range
    // ==========================

    @Schema(
            description = "Minimum Percentage",
            example = "60.00"
    )
    private BigDecimal minPercentage;

    @Schema(
            description = "Maximum Percentage",
            example = "90.00"
    )
    private BigDecimal maxPercentage;

    // ==========================
    // Obtained Marks Range
    // ==========================

    @Schema(
            description = "Minimum Obtained Marks",
            example = "40"
    )
    private Integer minObtainedMarks;

    @Schema(
            description = "Maximum Obtained Marks",
            example = "100"
    )
    private Integer maxObtainedMarks;

    // ==========================
    // Record Status
    // ==========================

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