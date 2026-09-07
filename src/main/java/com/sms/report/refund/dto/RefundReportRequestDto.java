package com.sms.report.refund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sms.enums.RecordStatus;
import com.sms.fee.refund.enums.RefundStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Refund Report Search Request")
public class RefundReportRequestDto {


	@Schema(description = "Student Name", example = "Rahul Kumar Sharma")
	private String studentName;


	@Schema(description = "Roll Number", example = "STU20260007")
	private String rollNumber;


	@Schema(description = "Admission Number", example = "ADM20260001")
	private String admissionNumber;


	@Schema(description = "Receipt Number", example = "SMS-REC-20260725-000001")
	private String receiptNumber;


	@Schema(description = "Refund Number", example = "SMS-REF-20260725-AB12CD")
	private String refundNumber;

	@Schema(description = "Receipt Date From", example = "2026-07-01")
	private LocalDate receiptDateFrom;

	@Schema(description = "Receipt Date", example = "2026-07-31")
	private LocalDate receiptDate;
	
	@Schema(description = "Receipt Date To", example = "2026-07-31")
	private LocalDate receiptDateTo;
	
	@Schema(description = "Refund Status")
	private RefundStatus refundStatus;


	@Schema(description = "Minimum Refund Amount", example = "1000")
	private BigDecimal minRefundAmount;


	@Schema(description = "Maximum Refund Amount", example = "5000")
	private BigDecimal maxRefundAmount;


	@Schema(description = "Refund Date From", example = "2026-07-01")
	private LocalDate refundDateFrom;


	@Schema(description = "Refund Date To", example = "2026-07-31")
	private LocalDate refundDateTo;


	@Schema(description = "Record Status")
	private RecordStatus status;

}