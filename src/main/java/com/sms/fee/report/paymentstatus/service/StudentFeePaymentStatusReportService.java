package com.sms.fee.report.paymentstatus.service;

import com.sms.dto.PageResponse;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportRequestDto;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportResponseDto;

public interface StudentFeePaymentStatusReportService {

	/**
	 * Search Payment Status Report
	 */
	PageResponse<PaymentStatusReportResponseDto> searchPaymentStatusReport(

			PaymentStatusReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction

	);

}
