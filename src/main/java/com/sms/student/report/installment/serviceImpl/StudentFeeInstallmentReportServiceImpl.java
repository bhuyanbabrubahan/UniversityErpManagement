package com.sms.student.report.installment.serviceImpl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.exception.BusinessException;
import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportRequestDto;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportResponseDto;
import com.sms.student.report.installment.mapper.StudentFeeInstallmentReportMapper;
import com.sms.student.report.installment.repository.StudentFeeInstallmentReportRepository;
import com.sms.student.report.installment.service.StudentFeeInstallmentReportService;
import com.sms.student.report.installment.specification.StudentFeeInstallmentReportSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentFeeInstallmentReportServiceImpl implements StudentFeeInstallmentReportService {

	private final StudentFeeInstallmentReportRepository repository;

	private final StudentFeeInstallmentReportMapper mapper;

	@Override
	public PageResponse<StudentFeeInstallmentReportResponseDto> getInstallmentReport(

			StudentFeeInstallmentReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction) {

		// =====================================================
		// Validate Date Range
		// =====================================================

		if (request.getDueDateFrom() != null

				&& request.getDueDateTo() != null

				&& request.getDueDateFrom().isAfter(request.getDueDateTo())) {

			throw new BusinessException("Due date from cannot be greater than due date to.");

		}

		if (request.getPaymentDateFrom() != null

				&& request.getPaymentDateTo() != null

				&& request.getPaymentDateFrom().isAfter(request.getPaymentDateTo())) {

			throw new BusinessException("Payment date from cannot be greater than payment date to.");

		}

		// =====================================================
		// Pageable
		// =====================================================

		Sort sort = direction.equalsIgnoreCase("asc")

				? Sort.by(sortBy).ascending()

				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		// =====================================================
		// Specification
		// =====================================================

		Specification<StudentFeeInstallment> specification =
				StudentFeeInstallmentReportSpecification.search(request);

		// =====================================================
		// Fetch Data
		// =====================================================

		Page<StudentFeeInstallment> result =
				repository.findAll(specification, pageable);

		// =====================================================
		// Entity -> DTO
		// =====================================================

		List<StudentFeeInstallmentReportResponseDto> content =

				result.getContent()

						.stream()

						.map(mapper::toDto)

						.toList();

		// =====================================================
		// Response
		// =====================================================

		PageResponse<StudentFeeInstallmentReportResponseDto> response = new PageResponse<>();

		response.setContent(content);

		response.setPageNumber(result.getNumber());

		response.setPageSize(result.getSize());

		response.setTotalElements(result.getTotalElements());

		response.setTotalPages(result.getTotalPages());

		response.setLast(result.isLast());

		return response;

	}

}