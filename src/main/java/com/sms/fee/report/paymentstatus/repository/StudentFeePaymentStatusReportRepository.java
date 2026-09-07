package com.sms.fee.report.paymentstatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sms.fee.entity.StudentFeePayment;

@Repository
public interface StudentFeePaymentStatusReportRepository
        extends JpaRepository<StudentFeePayment, Long>,
                JpaSpecificationExecutor<StudentFeePayment> {

}