package com.sms.fee.dto.report;

import java.time.LocalDate;

import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =====================================================
 * Fee Report Search Request DTO
 * =====================================================
 * Common search request used for all fee reports.
 *
 * Supported Reports:
 * - Fee Collection Report
 * - Pending Due Report
 * - Scholarship Report
 * - Refund Report
 * - Department Collection Report
 * - Course Collection Report
 * - Payment Mode Report
 * - Monthly Collection Report
 * =====================================================
 */
/**
 * Fee Report Search Request DTO
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Fee Report Search Request")
public class FeeReportSearchRequestDto {

    // =====================================================
    // Date Filters
    // =====================================================

    @Schema(description = "From payment date", example = "2026-07-01")
    private LocalDate fromDate;

    @Schema(description = "To payment date", example = "2026-07-31")
    private LocalDate toDate;

    // =====================================================
    // Master Filters
    // =====================================================

    @Schema(description = "Department ID", example = "1")
    private Long departmentId;

    @Schema(description = "Course ID", example = "2")
    private Long courseId;


    @Schema(description = "Student ID", example = "10")
    private Long studentId;

    @Schema(description = "Receipt ID")
    private Long receiptId;

    // =====================================================
    // Academic Filter
    // =====================================================

    @Schema(description = "Academic Year", example = "2026-2027")
    @Size(max = 20)
    private String academicYear;

    // =====================================================
    // Receipt Filters
    // =====================================================

    @Schema(description = "Receipt Number", example = "REC000001")
    @Size(max = 50)
    private String receiptNumber;

    @Schema(description = "Transaction Reference", example = "TXN987654321")
    @Size(max = 100)
    private String transactionReference;

    // =====================================================
    // Payment Filters
    // =====================================================

    @Schema(description = "Payment Mode")
    private PaymentMode paymentMode;

    @Schema(description = "Payment Status")
    private PaymentStatus paymentStatus;

    // =====================================================
    // Student Filters
    // =====================================================

    @Schema(description = "Student Name", example = "Rahul Sharma")
    @Size(max = 150)
    private String studentName;

    @Schema(description = "Roll Number", example = "CSE2026001")
    @Size(max = 50)
    private String rollNumber;

    @Schema(description = "Admission Number", example = "ADM2026001")
    @Size(max = 50)
    private String admissionNumber;

    // =====================================================
    // Pagination
    // =====================================================

    @Schema(description = "Page Number", example = "0", defaultValue = "0")
    @Builder.Default
    @Min(0)
    private Integer page = 0;

    @Schema(description = "Page Size", example = "10", defaultValue = "10")
    @Builder.Default
    @Min(1)
    private Integer size = 10;

	
	 // =====================================================
	 // Sorting
	 // =====================================================
	
	 @Schema(
	         description = "Sort Field",
	         example = "paymentDate",
	         defaultValue = "paymentDate"
	 )
	 @Builder.Default
	 private String sortBy = "paymentDate";
	
	
	
	 @Schema(
	         description = "Sort Direction",
	         example = "DESC",
	         allowableValues = {
	                 "ASC",
	                 "DESC"
	         },
	         defaultValue = "DESC"
	 )
	 @Builder.Default
	 private String sortDirection = "DESC";


}