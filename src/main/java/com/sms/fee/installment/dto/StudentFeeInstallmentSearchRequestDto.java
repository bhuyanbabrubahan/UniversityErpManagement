package com.sms.fee.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.installment.enums.InstallmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "StudentFeeInstallmentSearchRequestDto",
        description = "Request DTO for searching student fee installments."
)
public class StudentFeeInstallmentSearchRequestDto {

    // =====================================================
    // Installment
    // =====================================================

    @Schema(
            description = "Installment Number",
            example = "SMS-INS-20260721-000001"
    )
    private String installmentNumber;

    @Schema(
            description = "Installment Sequence",
            example = "1"
    )
    private Integer installmentSequence;

    // =====================================================
    // Student
    // =====================================================

    @Schema(
            description = "Student Name",
            example = "Rahul"
    )
    private String studentName;

    @Schema(
            description = "Roll Number",
            example = "STU20260007"
    )
    private String rollNumber;

    // =====================================================
    // Admission
    // =====================================================

    @Schema(
            description = "Admission Snapshot Number",
            example = "ADM20260001"
    )
    private String admissionSnapshotNumber;

    @Schema(
            description = "Academic Year",
            example = "2026-2027"
    )
    private String academicYear;

    @Schema(
            description = "Fee Payment Id",
            example = "1"
    )
    private Long paymentId;

    // =====================================================
    // Amount
    // =====================================================

    @DecimalMin(
            value = "0.00",
            message = "Minimum installment amount cannot be negative."
    )
    @Schema(
            description = "Minimum installment amount",
            example = "10000"
    )
    private BigDecimal minInstallmentAmount;

    @DecimalMin(
            value = "0.00",
            message = "Maximum installment amount cannot be negative."
    )
    @Schema(
            description = "Maximum installment amount",
            example = "50000"
    )
    private BigDecimal maxInstallmentAmount;

    // =====================================================
    // Status
    // =====================================================

    @Schema(
            description = "Installment Status",
            example = "PENDING"
    )
    private InstallmentStatus installmentStatus;

    @Schema(
            description = "Payment Mode",
            example = "ONLINE"
    )
    private PaymentMode paymentMode;

    // =====================================================
    // Due Date
    // =====================================================

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Due Date From",
            example = "2026-08-01"
    )
    private LocalDate dueDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Due Date To",
            example = "2026-08-31"
    )
    private LocalDate dueDateTo;

    // =====================================================
    // Payment Date
    // =====================================================

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Payment Date From",
            example = "2026-08-01"
    )
    private LocalDate paymentDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Payment Date To",
            example = "2026-08-31"
    )
    private LocalDate paymentDateTo;

    // =====================================================
    // Pagination
    // =====================================================

    @Min(
            value = 0,
            message = "Page number cannot be negative."
    )
    @Schema(
            description = "Page Number",
            example = "0",
            defaultValue = "0"
    )
    private Integer page = 0;

    @Min(
            value = 1,
            message = "Page size must be greater than zero."
    )
    @Schema(
            description = "Page Size",
            example = "10",
            defaultValue = "10"
    )
    private Integer size = 10;

    // =====================================================
    // Sorting
    // =====================================================

    @Schema(
            description = "Sort By Field",
            example = "dueDate",
            defaultValue = "id"
    )
    private String sortBy = "id";

    @Schema(
            description = "Sort Direction",
            example = "desc",
            allowableValues = {
                    "asc",
                    "desc"
            },
            defaultValue = "desc"
    )
    private String sortDirection = "desc";

}