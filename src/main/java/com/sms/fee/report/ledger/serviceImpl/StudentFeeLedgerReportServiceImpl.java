package com.sms.fee.report.ledger.serviceImpl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportRequestDto;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportResponseDto;
import com.sms.fee.report.ledger.mapper.StudentFeeLedgerReportMapper;
import com.sms.fee.report.ledger.repository.StudentFeeLedgerReportRepository;
import com.sms.fee.report.ledger.service.StudentFeeLedgerReportService;
import com.sms.fee.report.specification.StudentFeeLedgerReportSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentFeeLedgerReportServiceImpl implements StudentFeeLedgerReportService {

	private final StudentFeeLedgerReportRepository repository;

	private final StudentFeeLedgerReportMapper mapper;

	@Override
	public PageResponse<StudentFeeLedgerReportResponseDto> getStudentFeeLedgerReport(

			StudentFeeLedgerReportRequestDto request,

			int page,

			int size,

			String sortBy,

			String direction

	) {

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
				StudentFeeLedgerReportSpecification.search(request);

		// =====================================================
		// Exclude Deleted Records
		// =====================================================

		specification = specification.and(

				(root, query, cb) -> cb.notEqual(

						root.get("status"),

						RecordStatus.DELETED

				)

		);

		// =====================================================
		// Fetch Data
		// =====================================================

		Page<StudentFeePayment> result = repository.findAll(

				specification,

				pageable

		);

		// =====================================================
		// Entity -> DTO
		// =====================================================

		Page<StudentFeeLedgerReportResponseDto> dtoPage =
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