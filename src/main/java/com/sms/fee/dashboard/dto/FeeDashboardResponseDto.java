package com.sms.fee.dashboard.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "FeeDashboardResponseDto",
        description = "Response DTO containing finance dashboard summary details."
)
public class FeeDashboardResponseDto {

    // =====================================================
    // Collection
    // =====================================================

    @Schema(
            description = "Total fee collection",
            example = "2500000.00"
    )
    private BigDecimal totalFeeCollection;

    @Schema(
            description = "Today's fee collection",
            example = "35000.00"
    )
    private BigDecimal todayCollection;

    @Schema(
            description = "Current month's fee collection",
            example = "650000.00"
    )
    private BigDecimal monthlyCollection;

    // =====================================================
    // Pending
    // =====================================================

    @Schema(
            description = "Total pending due amount",
            example = "425000.00"
    )
    private BigDecimal totalPendingDue;

    // =====================================================
    // Payments
    // =====================================================

    @Schema(
            description = "Total number of fee payments",
            example = "1200"
    )
    private Long totalPayments;

    @Schema(
            description = "Total paid payments",
            example = "850"
    )
    private Long paidPayments;

    @Schema(
            description = "Total partially paid payments",
            example = "220"
    )
    private Long partialPayments;

    @Schema(
            description = "Total pending payments",
            example = "110"
    )
    private Long pendingPayments;

    @Schema(
            description = "Total cancelled payments",
            example = "20"
    )
    private Long cancelledPayments;

    // =====================================================
    // Payment Mode
    // =====================================================

    @Schema(
            description = "Total cash collection",
            example = "450000.00"
    )
    private BigDecimal cashCollection;

    @Schema(
            description = "Total online banking collection",
            example = "900000.00"
    )
    private BigDecimal onlineCollection;

    @Schema(
            description = "Total UPI collection",
            example = "1050000.00"
    )
    private BigDecimal upiCollection;

    @Schema(
            description = "Total cheque collection",
            example = "100000.00"
    )
    private BigDecimal chequeCollection;

    // =====================================================
    // Scholarship
    // =====================================================

    @Schema(
            description = "Total scholarship amount granted",
            example = "350000.00"
    )
    private BigDecimal totalScholarshipAmount;

    @Schema(
            description = "Total number of students receiving scholarship",
            example = "180"
    )
    private Long totalScholarshipStudents;
    
    
    @Schema(
            description = "Total number of generated fee receipts",
            example = "1150"
    )
    private Long totalReceipts;


    // =====================================================
    // Refund Summary
    // =====================================================
    private BigDecimal totalRefundAmount;
    private Long todayRefunds;
    private Long monthlyRefunds;
    private Long completedRefunds;
    private Long pendingRefunds;
    private Long rejectedRefunds;
    private Long cancelledRefunds;

    // =====================================================
    // Installment Summary
    // =====================================================
    private Long totalInstallments;
    private Long paidInstallments;
    private Long pendingInstallments;
    private Long overdueInstallments;

    // =====================================================
    // Receipt Summary
    // =====================================================
    private Long todayReceipts;
    private Long monthlyReceipts;
    private Long generatedToday;
    private Long downloadedToday;

    // =====================================================
    // Student Fee Summary
    // =====================================================
    private Long totalStudentsWithFees;
    private Long paidStudents;
    private Long partialStudents;
    private Long pendingStudents;

    // =====================================================
    // Alerts
    // =====================================================
    private Long overdueInstallmentAlerts;
    private Long pendingRefundApprovals;
    private Long failedPayments;

    // =====================================================
    // Recent Activity
    // =====================================================
    private java.util.List<FeeActivityItemDto> recentPayments;
    private java.util.List<FeeActivityItemDto> recentReceipts;
    private java.util.List<FeeActivityItemDto> recentRefunds;
    private java.util.List<FeeActivityItemDto> recentScholarships;
    private java.util.List<FeeDueStudentDto> highestDueStudents;


    @Schema(
            description = "Average fee collected per payment",
            example = "4583.33"
    )
    private BigDecimal averageCollection;


    @Schema(
            description = "Fee collection percentage",
            example = "92.50"
    )
    private BigDecimal collectionPercentage;

}