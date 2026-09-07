package com.sms.fee.report.dailycollection.serviceImpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.dto.PageResponse;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.dailycollection.dto.DailyCollectionReportResponseDto;
import com.sms.fee.report.dailycollection.mapper.DailyCollectionReportMapper;
import com.sms.fee.report.dailycollection.repository.StudentDailyCollectionReportRepository;
import com.sms.fee.report.dailycollection.service.StudentDailyCollectionReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentDailyCollectionReportServiceImpl implements StudentDailyCollectionReportService {

	private final StudentDailyCollectionReportRepository repository;

	private final DailyCollectionReportMapper mapper;

	@Override
	public PageResponse<DailyCollectionReportResponseDto> getDailyCollectionReport(

			LocalDate paymentDate,

			int page,

			int size,

			String sortBy,

			String direction

	) {

		// =====================================================
		// Default Today's Date
		// =====================================================

		if (paymentDate == null) {

			paymentDate = LocalDate.now();

		}

		// =====================================================
		// Sorting
		// =====================================================

		Sort sort = direction.equalsIgnoreCase("asc")

				? Sort.by(sortBy).ascending()

				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		// =====================================================
		// Fetch Daily Collection
		// =====================================================

		Page<StudentFeePayment> result = repository.findByPaymentDateAndStatusNot(

				paymentDate,

				RecordStatus.DELETED,

				pageable

		);

		// =====================================================
		// Entity -> DTO
		// =====================================================

		Page<DailyCollectionReportResponseDto> dtoPage = result.map(mapper::toDto);

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