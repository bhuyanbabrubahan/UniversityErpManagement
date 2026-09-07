package com.sms.fee.dto;




import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.FeeType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "FeeStructureResponseDto",
        description = "Response DTO containing fee structure details."
)
public class FeeStructureResponseDto {



    // ==========================
    // ID
    // ==========================


    @Schema(
            description = "Fee structure ID",
            example = "1"
    )
    private Long id;



    // ==========================
    // Course
    // ==========================


    @Schema(
            description = "Course ID",
            example = "4"
    )
    private Long courseId;



    @Schema(
            description = "Course name",
            example = "Information Technology"
    )
    private String courseName;



    // ==========================
    // Semester
    // ==========================


    @Schema(
            description = "Semester ID",
            example = "21"
    )
    private Long semesterId;



    @Schema(
            description = "Semester name",
            example = "Semester I"
    )
    private String semesterName;



    // ==========================
    // Fee Details
    // ==========================


    @Schema(
            description = "Fee type",
            example = "TUITION_FEE"
    )
    private FeeType feeType;



    @Schema(
            description = "Fee amount",
            example = "50000.00"
    )
    private BigDecimal amount;



    // ==========================
    // Academic Year
    // ==========================


    @Schema(
            description = "Academic year",
            example = "2026-2027"
    )
    private String academicYear;



    // ==========================
    // Status
    // ==========================


    @Schema(
            description = "Record status",
            example = "ACTIVE"
    )
    private RecordStatus status;



    // ==========================
    // Audit
    // ==========================


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


}