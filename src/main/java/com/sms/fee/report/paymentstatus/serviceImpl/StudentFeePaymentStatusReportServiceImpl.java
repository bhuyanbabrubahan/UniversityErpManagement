package com.sms.fee.report.paymentstatus.serviceImpl;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportRequestDto;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportResponseDto;
import com.sms.fee.report.paymentstatus.mapper.PaymentStatusReportMapper;
import com.sms.fee.report.paymentstatus.repository.StudentFeePaymentStatusReportRepository;
import com.sms.fee.report.paymentstatus.service.StudentFeePaymentStatusReportService;
import com.sms.fee.report.paymentstatus.specification.StudentFeePaymentStatusReportSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentFeePaymentStatusReportServiceImpl
		implements StudentFeePaymentStatusReportService {

	private final StudentFeePaymentStatusReportRepository repository;

	private final PaymentStatusReportMapper mapper;

	@Override
	public PageResponse<PaymentStatusReportResponseDto> searchPaymentStatusReport(

			PaymentStatusReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction) {

		// =====================================================
		// Sorting
		// =====================================================

		Sort sort = direction.equalsIgnoreCase("asc")

				? Sort.by(sortBy).ascending()

				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		// =====================================================
		// Specification
		// =====================================================

		Specification<StudentFeePayment> specification =
				StudentFeePaymentStatusReportSpecification.search(request);

		// =====================================================
		// Fetch Data
		// =====================================================

		Page<StudentFeePayment> result =
				repository.findAll(specification, pageable);

		// =====================================================
		// Entity -> DTO
		// =====================================================

		List<PaymentStatusReportResponseDto> content =
				result.getContent()
				      .stream()
				      .map(mapper::toDto)
				      .toList();

		// =====================================================
		// Response
		// =====================================================

		PageResponse<PaymentStatusReportResponseDto> response =
				new PageResponse<>();

		response.setContent(content);

		response.setPageNumber(result.getNumber());

		response.setPageSize(result.getSize());

		response.setTotalElements(result.getTotalElements());

		response.setTotalPages(result.getTotalPages());

		response.setLast(result.isLast());

		return response;
	}

}