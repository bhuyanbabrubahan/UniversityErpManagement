package com.sms.fee.dto;


import java.math.BigDecimal;

import com.sms.fee.enums.FeeType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Schema(
        name = "FeeStructureRequestDto",
        description = "Request DTO used to create or update fee structure."
)
public class FeeStructureRequestDto {



    // ==========================
    // Course
    // ==========================


    @Schema(
            description = "Course ID for which fee is applicable",
            example = "4",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Course id is required"
    )
    private Long courseId;



    // ==========================
    // Semester
    // ==========================


    @Schema(
            description = "Semester ID for fee structure",
            example = "21",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Semester id is required"
    )
    private Long semesterId;



    // ==========================
    // Fee Type
    // ==========================


    @Schema(
            description = "Type of fee",
            example = "TUITION_FEE",
            allowableValues = {
                    "TUITION_FEE",
                    "LAB_FEE",
                    "EXAM_FEE",
                    "LIBRARY_FEE",
                    "HOSTEL_FEE",
                    "OTHER"
            },
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Fee type is required"
    )
    private FeeType feeType;




    // ==========================
    // Amount
    // ==========================


    @Schema(
            description = "Fee amount",
            example = "50000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Amount is required"
    )
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Amount must be greater than zero"
    )
    private BigDecimal amount;



    // ==========================
    // Academic Year
    // ==========================


    @Schema(
            description = "Academic year",
            example = "2026-2027",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Academic year is required"
    )
    @Size(
            max = 20,
            message = "Academic year cannot exceed 20 characters"
    )
    private String academicYear;


}