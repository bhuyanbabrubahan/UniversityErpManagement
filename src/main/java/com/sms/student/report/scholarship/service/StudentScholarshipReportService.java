package com.sms.student.report.scholarship.service;

import com.sms.dto.PageResponse;
import com.sms.student.report.scholarship.dto.ScholarshipReportRequestDto;
import com.sms.student.report.scholarship.dto.ScholarshipReportResponseDto;

public interface StudentScholarshipReportService {

    PageResponse<ScholarshipReportResponseDto> getScholarshipReport(
            ScholarshipReportRequestDto request,
            int page,
            int size,
            String sortBy,
            String direction
    );

}