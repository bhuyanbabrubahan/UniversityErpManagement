package com.sms.fee.report.dailycollection.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;


public interface StudentDailyCollectionReportRepository
		extends JpaRepository<StudentFeePayment, Long>,
		JpaSpecificationExecutor<StudentFeePayment> {

	// =====================================================
	// Daily Collection Report
	// =====================================================

	Page<StudentFeePayment> findByPaymentDateAndStatusNot(

			LocalDate paymentDate,

			RecordStatus status,

			Pageable pageable

	);

}