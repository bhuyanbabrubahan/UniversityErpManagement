package com.sms.report.refund.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.report.refund.dto.RefundReportResponseDto;



@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RefundReportMapper {



	@Mapping(
			target = "refundNumber",
			source = "refundNumber"
	)

	@Mapping(
			target = "refundDate",
			source = "refundDate"
	)


	@Mapping(
			target = "studentName",
			source = "studentName"
	)


	@Mapping(
			target = "rollNumber",
			source = "rollNumber"
	)


	@Mapping(
			target = "admissionNumber",
			source = "admissionNumber"
	)



	@Mapping(
			target = "receiptNumber",
			source = "receiptNumber"
	)


	@Mapping(
			target = "receiptDate",
			source = "receiptDate"
	)



	@Mapping(
			target = "refundAmount",
			source = "refundAmount"
	)



	@Mapping(
			target = "refundMode",
			source = "refundMode"
	)



	@Mapping(
			target = "refundStatus",
			source = "refundStatus"
	)



	@Mapping(
			target = "refundReason",
			source = "refundReason"
	)



	@Mapping(
			target = "paymentTransactionReference",
			source = "paymentTransactionReference"
	)



	@Mapping(
			target = "refundTransactionReference",
			source = "refundTransactionReference"
	)



	@Mapping(
			target = "bankReference",
			source = "bankReference"
	)



	@Mapping(
			target = "remarks",
			source = "remarks"
	)



	@Mapping(
			target = "requestedBy",
			source = "requestedBy"
	)



	@Mapping(
			target = "requestedDate",
			source = "requestedDate"
	)



	@Mapping(
			target = "approvedBy",
			source = "approvedBy"
	)



	@Mapping(
			target = "approvedDate",
			source = "approvedDate"
	)



	@Mapping(
			target = "completedBy",
			source = "completedBy"
	)



	@Mapping(
			target = "completedDate",
			source = "completedDate"
	)



	@Mapping(
			target = "rejectedBy",
			source = "rejectedBy"
	)



	@Mapping(
			target = "rejectedDate",
			source = "rejectedDate"
	)



	@Mapping(
			target = "rejectionReason",
			source = "rejectionReason"
	)



	@Mapping(
			target = "cancelledBy",
			source = "cancelledBy"
	)



	@Mapping(
			target = "cancelledDate",
			source = "cancelledDate"
	)



	@Mapping(
			target = "cancellationReason",
			source = "cancellationReason"
	)



	RefundReportResponseDto toDto(StudentFeeRefund entity);


}