package com.sms.report.refund.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sms.enums.RecordStatus;
import com.sms.fee.refund.entity.StudentFeeRefund;

@Repository
public interface StudentFeeRefundReportRepository
		extends JpaRepository<StudentFeeRefund, Long>, JpaSpecificationExecutor<StudentFeeRefund> {

	Page<StudentFeeRefund> findByStatusNot(

			RecordStatus status,

			Pageable pageable

	);

}