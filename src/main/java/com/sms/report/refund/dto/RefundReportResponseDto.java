package com.sms.report.refund.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.fee.refund.enums.RefundMode;
import com.sms.fee.refund.enums.RefundStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Refund Report Response")
public class RefundReportResponseDto {


	@Schema(description = "Refund Number")
	private String refundNumber;


	@Schema(description = "Refund Date")
	private LocalDate refundDate;


	@Schema(description = "Student Name")
	private String studentName;


	@Schema(description = "Roll Number")
	private String rollNumber;


	@Schema(description = "Admission Number")
	private String admissionNumber;


	@Schema(description = "Receipt Number")
	private String receiptNumber;


	@Schema(description = "Receipt Date")
	private LocalDate receiptDate;


	@Schema(description = "Refund Amount")
	private BigDecimal refundAmount;


	@Schema(description = "Refund Mode")
	private RefundMode refundMode;


	@Schema(description = "Refund Status")
	private RefundStatus refundStatus;


	@Schema(description = "Refund Reason")
	private String refundReason;


	@Schema(description = "Payment Transaction Reference")
	private String paymentTransactionReference;


	@Schema(description = "Refund Transaction Reference")
	private String refundTransactionReference;


	@Schema(description = "Bank Reference")
	private String bankReference;


	@Schema(description = "Remarks")
	private String remarks;


	@Schema(description = "Requested By")
	private String requestedBy;


	@Schema(description = "Requested Date")
	private LocalDateTime requestedDate;


	@Schema(description = "Approved By")
	private String approvedBy;


	@Schema(description = "Approved Date")
	private LocalDateTime approvedDate;


	@Schema(description = "Completed By")
	private String completedBy;


	@Schema(description = "Completed Date")
	private LocalDateTime completedDate;


	@Schema(description = "Rejected By")
	private String rejectedBy;


	@Schema(description = "Rejected Date")
	private LocalDateTime rejectedDate;


	@Schema(description = "Rejection Reason")
	private String rejectionReason;


	@Schema(description = "Cancelled By")
	private String cancelledBy;


	@Schema(description = "Cancelled Date")
	private LocalDateTime cancelledDate;


	@Schema(description = "Cancellation Reason")
	private String cancellationReason;

}