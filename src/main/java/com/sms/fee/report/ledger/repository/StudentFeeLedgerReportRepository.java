package com.sms.fee.report.ledger.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.fee.entity.StudentFeePayment;


public interface StudentFeeLedgerReportRepository
		extends JpaRepository<StudentFeePayment, Long>,
		JpaSpecificationExecutor<StudentFeePayment> {

}