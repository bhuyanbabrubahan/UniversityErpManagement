package com.sms.scholarship.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Student Scholarship Request DTO")
public class StudentScholarshipRequestDto {

    @NotNull(message = "Student id is required")
    @Schema(
            description = "Student ID",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long studentId;

    @NotNull(message = "Admission id is required")
    @Schema(
            description = "Admission ID",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long admissionId;

    @NotBlank(message = "Scholarship name is required")
    @Size(
            max = 100,
            message = "Scholarship name max 100 characters"
    )
    @Schema(
            description = "Scholarship name",
            example = "Merit Scholarship"
    )
    private String scholarshipName;

    @NotNull(message = "Scholarship type is required")
    @Schema(
            description = "Scholarship type",
            example = "PERCENTAGE",
            allowableValues = {
                    "FIXED_AMOUNT",
                    "PERCENTAGE",
                    "FULL_FEE_WAIVER",
                    "PARTIAL_FEE_WAIVER"
            }
    )
    private ScholarshipType scholarshipType;

    @NotNull(message = "Scholarship value is required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Scholarship value cannot be negative"
    )
    @Schema(
            description = "Scholarship value",
            example = "50.00"
    )
    private BigDecimal scholarshipValue;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Requested amount cannot be negative"
    )
    @Schema(
            description = "Requested scholarship amount",
            example = "25000.00"
    )
    private BigDecimal requestedAmount;

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Approved amount cannot be negative"
    )
    @Schema(
            description = "Approved scholarship amount",
            example = "20000.00"
    )
    private BigDecimal approvedAmount;

    @NotBlank(message = "Academic year is required")
    @Size(
            max = 20,
            message = "Academic year cannot exceed 20 characters"
    )
    @Schema(
            description = "Academic year",
            example = "2026-2027"
    )
    private String academicYear;

    @Schema(
            description = "Scholarship approval date",
            example = "2026-07-20"
    )
    private LocalDate approvalDate;

    @Size(
            max = 500,
            message = "Remarks cannot exceed 500 characters"
    )
    @Schema(
            description = "Remarks",
            example = "Merit based scholarship approved."
    )
    private String remarks;

    @Size(
            max = 500,
            message = "Rejection reason cannot exceed 500 characters"
    )
    @Schema(
            description = "Reason for scholarship rejection",
            example = "Required documents are incomplete."
    )
    private String rejectionReason;

    @NotNull(message = "Scholarship status is required")
    @Schema(
            description = "Scholarship status",
            example = "PENDING",
            allowableValues = {
                    "PENDING",
                    "APPROVED",
                    "REJECTED"
            }
    )
    private ScholarshipStatus scholarshipStatus;

}