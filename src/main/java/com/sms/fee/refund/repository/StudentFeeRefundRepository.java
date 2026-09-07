package com.sms.fee.refund.repository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.enums.RecordStatus;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.enums.RefundStatus;



public interface StudentFeeRefundRepository
        extends JpaRepository<StudentFeeRefund, Long>,
                JpaSpecificationExecutor<StudentFeeRefund> {



    // =====================================================
    // Find By Id (Soft Delete Support)
    // =====================================================


    Optional<StudentFeeRefund> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );





    // =====================================================
    // Get All Active Refunds
    // =====================================================


    Page<StudentFeeRefund> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );

    List<StudentFeeRefund> findByStudentIdAndStatusNotOrderByRefundDateDesc(
            Long studentId,
            RecordStatus status
    );



    // =====================================================
    // Duplicate Refund Number Check
    // =====================================================


    boolean existsByRefundNumberAndStatusNot(
            String refundNumber,
            RecordStatus status
    );


    boolean existsByPaymentIdAndRefundStatusInAndStatusNot(
            Long paymentId,
            List<RefundStatus> refundStatuses,
            RecordStatus status
    );



    // =====================================================
    // Student Refund History
    // =====================================================


    Page<StudentFeeRefund> findByStudentIdAndStatusNot(
            Long studentId,
            RecordStatus status,
            Pageable pageable
    );





    // =====================================================
    // Payment Refund History
    // =====================================================


    Page<StudentFeeRefund> findByPaymentIdAndStatusNot(
            Long paymentId,
            RecordStatus status,
            Pageable pageable
    );
    
    
    
    
 // =====================================================
 // Total Refunded Amount By Payment
 // =====================================================

 @Query("""
         SELECT COALESCE(SUM(r.refundAmount), 0)
         FROM StudentFeeRefund r
         WHERE r.payment.id = :paymentId
         AND r.status <> :status
         AND (:refundId IS NULL OR r.id <> :refundId)
         """)
 BigDecimal getTotalRefundAmountByPayment(

         @Param("paymentId")
         Long paymentId,

         @Param("status")
         RecordStatus status,

         @Param("refundId")
         Long refundId

 );



 boolean existsByPaymentIdAndRefundStatusAndStatusNot(
	        Long paymentId,
	        RefundStatus refundStatus,
	        RecordStatus status
	);
 
 
 boolean existsByPaymentIdAndStatusNot(
	        Long id,
	        RecordStatus status
	);

 
 
 @Query("""
		 SELECT COALESCE(SUM(r.refundAmount),0)
		 FROM StudentFeeRefund r
		 WHERE r.status <> :status
		 """)
		 BigDecimal getTotalRefundAmount(
		         @Param("status") RecordStatus status
		 );


		 long countByStatusNot(RecordStatus status);
		 long countByStudentId(Long studentId);

	long countByRefundStatusAndStatusNot(RefundStatus refundStatus, RecordStatus status);
	long countByRefundDateAndStatusNot(java.time.LocalDate refundDate, RecordStatus status);
	long countByRefundDateBetweenAndStatusNot(java.time.LocalDate fromDate, java.time.LocalDate toDate, RecordStatus status);
	List<StudentFeeRefund> findTop10ByStatusNotOrderByRefundDateDesc(RecordStatus status);

 
}