package com.sms.fee.report.ledger.service;

import com.sms.dto.PageResponse;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportRequestDto;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportResponseDto;

public interface StudentFeeLedgerReportService {

	// =====================================================
	// Student Fee Ledger Report
	// =====================================================

	PageResponse<StudentFeeLedgerReportResponseDto> getStudentFeeLedgerReport(

			StudentFeeLedgerReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction

	);

}