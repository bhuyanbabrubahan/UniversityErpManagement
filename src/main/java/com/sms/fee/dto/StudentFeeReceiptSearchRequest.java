package com.sms.fee.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
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

@Schema(
        name = "StudentFeeReceiptSearchRequest",
        description = 
        "Dynamic search request for student fee receipts using receipt, payment, student, scholarship, amount and date filters."
)

public class StudentFeeReceiptSearchRequest {



    // =====================================================
    // Receipt Filter
    // =====================================================


    @Schema(
            description = "Search by receipt number",
            example = "SMS-REC-20260721-000001"
    )
    private String receiptNumber;




    // =====================================================
    // Payment Filter
    // =====================================================


    @Schema(
            description = "Search by payment id",
            example = "10"
    )
    private Long paymentId;



    @Schema(
            description = "Search by payment status",
            example = "PAID"
    )
    private PaymentStatus paymentStatus;



    @Schema(
            description = "Search by payment mode",
            example = "UPI"
    )
    private PaymentMode paymentMode;



    // =====================================================
    // Student Filter
    // =====================================================


    @Schema(
            description = "Search by student id",
            example = "1"
    )
    private Long studentId;



    @Schema(
            description = "Search by student name",
            example = "Rahul"
    )
    private String studentName;



    @Schema(
            description = "Search by roll number",
            example = "CS2026001"
    )
    private String rollNumber;




    // =====================================================
    // Admission Filter
    // =====================================================


    @Schema(
            description = "Search by admission id",
            example = "5"
    )
    private Long admissionId;



    @Schema(
            description = "Search by admission number",
            example = "ADM-2026-001"
    )
    private String admissionNumber;



    @Schema(
            description = "Search by academic year",
            example = "2026-2027"
    )
    private String academicYear;




    // =====================================================
    // Course Filter
    // =====================================================


    @Schema(
            description = "Search by course name",
            example = "B.Tech Computer Science"
    )
    private String courseName;



    @Schema(
            description = "Search by department name",
            example = "Computer Science"
    )
    private String departmentName;



    @Schema(
            description = "Search by semester name",
            example = "Semester 1"
    )
    private String semesterName;




    // =====================================================
    // Scholarship Filter
    // =====================================================


    @Schema(
            description = "Search by scholarship id",
            example = "2"
    )
    private Long scholarshipId;




    // =====================================================
    // Amount Filter
    // =====================================================


    @DecimalMin(
            value = "0.0",
            message = "Minimum paid amount cannot be negative"
    )
    @Schema(
            description = "Minimum paid amount",
            example = "10000"
    )
    private BigDecimal minPaidAmount;



    @DecimalMin(
            value = "0.0",
            message = "Maximum paid amount cannot be negative"
    )
    @Schema(
            description = "Maximum paid amount",
            example = "50000"
    )
    private BigDecimal maxPaidAmount;




    // =====================================================
    // Receipt Date Range
    // =====================================================


    @PastOrPresent(
            message = "Receipt date cannot be future"
    )
    @Schema(
            description = "Receipt date from",
            example = "2026-07-01"
    )
    private LocalDate receiptDateFrom;



    @PastOrPresent(
            message = "Receipt date cannot be future"
    )
    @Schema(
            description = "Receipt date to",
            example = "2026-07-31"
    )
    private LocalDate receiptDateTo;


	    
	 // =====================================================
	 // Total Fee Range
	 // =====================================================
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Minimum total fee amount",
	         example = "10000.00"
	 )
	 private BigDecimal minTotalFee;
	
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Maximum total fee amount",
	         example = "100000.00"
	 )
	 private BigDecimal maxTotalFee;
	
	
	 // =====================================================
	 // Due Amount Range
	 // =====================================================
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Minimum due amount",
	         example = "0.00"
	 )
	 private BigDecimal minDueAmount;
	
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Maximum due amount",
	         example = "50000.00"
	 )
	 private BigDecimal maxDueAmount;
	
	
	 // =====================================================
	 // Final Payable Amount Range
	 // =====================================================
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Minimum final payable amount after discount/scholarship",
	         example = "25000.00"
	 )
	 private BigDecimal minFinalPayableAmount;
	
	
	 @DecimalMin(value = "0.0")
	 @Schema(
	         description = "Maximum final payable amount after discount/scholarship",
	         example = "75000.00"
	 )
	 private BigDecimal maxFinalPayableAmount;


    // =====================================================
    // Record Status
    // =====================================================


    @Schema(
            description = "Search by record status",
            example = "ACTIVE",
            allowableValues = {
                    "ACTIVE",
                    "INACTIVE",
                    "DELETED"
            }
    )
    private RecordStatus status;


}