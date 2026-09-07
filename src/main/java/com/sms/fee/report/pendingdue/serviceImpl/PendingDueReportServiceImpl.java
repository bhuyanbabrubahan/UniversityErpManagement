package com.sms.fee.report.pendingdue.serviceImpl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.fee.report.pendingdue.dto.PendingDueReportResponseDto;
import com.sms.fee.report.pendingdue.repository.PendingDueReportRepository;
import com.sms.fee.report.pendingdue.service.PendingDueReportService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class PendingDueReportServiceImpl
        implements PendingDueReportService {



    private final PendingDueReportRepository repository;



    // =====================================================
    // Pending Due Report
    // =====================================================


    @Override
    @Transactional(readOnly = true)
    public PageResponse<PendingDueReportResponseDto> getPendingDueReport(
            int page,
            int size
    ) {



        // =====================================================
        // Create Pageable
        // =====================================================

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );



        // =====================================================
        // Fetch Report Data
        // =====================================================

        Page<PendingDueReportResponseDto> responsePage =
                repository.getPendingDueReport(
                        pageable
                );



        // =====================================================
        // Convert Page To Common Response
        // =====================================================

        return new PageResponse<>(

                responsePage.getContent(),

                responsePage.getNumber(),

                responsePage.getSize(),

                responsePage.getTotalElements(),

                responsePage.getTotalPages(),

                responsePage.isLast()

        );

    }


}