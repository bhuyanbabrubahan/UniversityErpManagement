package com.sms.student.report.scholarship.serviceImpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.exception.BusinessException;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.student.report.scholarship.dto.ScholarshipReportRequestDto;
import com.sms.student.report.scholarship.dto.ScholarshipReportResponseDto;
import com.sms.student.report.scholarship.mapper.StudentScholarshipReportMapper;
import com.sms.student.report.scholarship.repository.StudentScholarshipReportRepository;
import com.sms.student.report.scholarship.service.StudentScholarshipReportService;
import com.sms.student.report.scholarship.specification.StudentScholarshipReportSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentScholarshipReportServiceImpl implements StudentScholarshipReportService {

    private final StudentScholarshipReportRepository reportRepository;

    private final StudentScholarshipReportMapper mapper;

    @Override
    public PageResponse<ScholarshipReportResponseDto> getScholarshipReport(
            ScholarshipReportRequestDto request,
            int page,
            int size,
            String sortBy,
            String direction) {

        // =====================================================
        // Validate Date Range
        // =====================================================

        LocalDate from = request.getApprovalDateFrom();
        LocalDate to = request.getApprovalDateTo();

        if (from != null && to != null && from.isAfter(to)) {

            throw new BusinessException(
                    "Approval From Date cannot be greater than Approval To Date.");
        }

        // =====================================================
        // Sorting
        // =====================================================

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // =====================================================
        // Specification
        // =====================================================

        Specification<StudentScholarship> specification =
                StudentScholarshipReportSpecification.search(request);

        // =====================================================
        // Fetch Data
        // =====================================================

        Page<StudentScholarship> result =
                reportRepository.findAll(specification, pageable);

        // =====================================================
        // Entity -> DTO
        // =====================================================

        Page<ScholarshipReportResponseDto> dtoPage =
                result.map(mapper::toDto);

        // =====================================================
        // Response
        // =====================================================

        return new PageResponse<>(

                dtoPage.getContent(),

                dtoPage.getNumber(),

                dtoPage.getSize(),

                dtoPage.getTotalElements(),

                dtoPage.getTotalPages(),

                dtoPage.isLast()

        );
    }

}