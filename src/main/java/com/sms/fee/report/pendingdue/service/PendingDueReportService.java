package com.sms.fee.report.pendingdue.service;


import com.sms.dto.PageResponse;
import com.sms.fee.report.pendingdue.dto.PendingDueReportResponseDto;


public interface PendingDueReportService {


    // =====================================================
    // Pending Due Report
    // =====================================================

    PageResponse<PendingDueReportResponseDto> getPendingDueReport(
            int page,
            int size
    );


}