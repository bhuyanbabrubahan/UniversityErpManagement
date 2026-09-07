package com.sms.student.report.installment.service;

import com.sms.dto.PageResponse;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportRequestDto;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportResponseDto;

public interface StudentFeeInstallmentReportService {

	PageResponse<StudentFeeInstallmentReportResponseDto> getInstallmentReport(

			StudentFeeInstallmentReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction

	);

}