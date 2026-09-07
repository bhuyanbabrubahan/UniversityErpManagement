package com.sms.report.refund.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.repository.StudentFeeRefundRepository;
import com.sms.report.refund.dto.RefundReportRequestDto;
import com.sms.report.refund.dto.RefundReportResponseDto;
import com.sms.report.refund.mapper.RefundReportMapper;
import com.sms.report.refund.service.StudentFeeRefundReportService;
import com.sms.report.refund.specification.StudentFeeRefundReportSpecification;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentFeeRefundReportServiceImpl implements StudentFeeRefundReportService {

    private final StudentFeeRefundRepository refundRepository;

    private final RefundReportMapper mapper;

    @Override
    public PageResponse<RefundReportResponseDto> getRefundReport(
            RefundReportRequestDto request,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<StudentFeeRefund> specification =
        		StudentFeeRefundReportSpecification.search(request);

        Page<StudentFeeRefund> result =
                refundRepository.findAll(specification, pageable);

        List<RefundReportResponseDto> content =
                result.getContent()
                      .stream()
                      .map(mapper::toDto)
                      .toList();

        PageResponse<RefundReportResponseDto> response = new PageResponse<>();

        response.setContent(content);
        response.setPageNumber(result.getNumber());
        response.setPageSize(result.getSize());
        response.setTotalElements(result.getTotalElements());
        response.setTotalPages(result.getTotalPages());
        response.setLast(result.isLast());

        return response;
    }

}