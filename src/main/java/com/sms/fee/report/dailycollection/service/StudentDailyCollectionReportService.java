package com.sms.fee.report.dailycollection.service;

import java.time.LocalDate;

import com.sms.dto.PageResponse;
import com.sms.fee.report.dailycollection.dto.DailyCollectionReportResponseDto;

public interface StudentDailyCollectionReportService {

	// =====================================================
	// Daily Collection Report
	// =====================================================

	PageResponse<DailyCollectionReportResponseDto> getDailyCollectionReport(

			LocalDate paymentDate,

			int page,

			int size,

			String sortBy,

			String direction

	);

}