package com.sms.fee.dto;


import com.sms.enums.RecordStatus;
import com.sms.fee.enums.FeeType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "FeeStructureSearchRequest",
        description = "Request DTO used to search fee structures using optional filters."
)
public class FeeStructureSearchRequest {

    // ==========================================================
    // Course
    // ==========================================================

    @Schema(
            description = "Search by course ID",
            example = "4"
    )
    private Long courseId;

    // ==========================================================
    // Semester
    // ==========================================================

    @Schema(
            description = "Search by semester ID",
            example = "21"
    )
    private Long semesterId;

    // ==========================================================
    // Fee Type
    // ==========================================================

    @Schema(
            description = "Search by fee type",
            example = "TUITION_FEE",
            allowableValues = {
                    "TUITION_FEE",
                    "LAB_FEE",
                    "EXAM_FEE",
                    "LIBRARY_FEE",
                    "HOSTEL_FEE",
                    "OTHER"
            }
    )
    private FeeType feeType;

    // ==========================================================
    // Academic Year
    // ==========================================================

    @Schema(
            description = "Search by academic year",
            example = "2026-2027"
    )
    private String academicYear;

    // ==========================================================
    // Status
    // ==========================================================

    @Schema(
            description = "Record status. Default ACTIVE records are returned.",
            example = "ACTIVE",
            allowableValues = {
                    "ACTIVE",
                    "INACTIVE",
                    "DELETED"
            }
    )
    private RecordStatus status;

}
