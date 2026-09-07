package com.sms.report.refund.service;

import com.sms.dto.PageResponse;
import com.sms.report.refund.dto.RefundReportRequestDto;
import com.sms.report.refund.dto.RefundReportResponseDto;

public interface StudentFeeRefundReportService {

	PageResponse<RefundReportResponseDto> getRefundReport(

			RefundReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction

	);

}