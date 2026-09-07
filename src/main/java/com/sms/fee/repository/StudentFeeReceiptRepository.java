package com.sms.fee.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeeReceipt;



public interface StudentFeeReceiptRepository
        extends JpaRepository<StudentFeeReceipt, Long>,
                JpaSpecificationExecutor<StudentFeeReceipt> {



    // =====================================================
    // Find Receipt By Id (Soft Delete Support)
    // =====================================================


    Optional<StudentFeeReceipt> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );





    // =====================================================
    // Get All Active Receipts
    // =====================================================


    Page<StudentFeeReceipt> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );





    // =====================================================
    // Duplicate Receipt Validation
    // =====================================================


    boolean existsByPaymentIdAndStatusNot(
            Long paymentId,
            RecordStatus status
    );





    // =====================================================
    // Receipt Number Validation
    // =====================================================


    Optional<StudentFeeReceipt> findByReceiptNumberAndStatusNot(
            String receiptNumber,
            RecordStatus status
    );





    // =====================================================
    // Latest Receipt For Number Generation
    // =====================================================


    Optional<StudentFeeReceipt> findTopByOrderByIdDesc();





    // =====================================================
    // Dashboard Statistics
    // =====================================================


    long countByStatusNot(
            RecordStatus status
    );





    // =====================================================
    // Date Range Report Support
    // =====================================================


    long countByReceiptDateBetweenAndStatusNot(
            java.time.LocalDate fromDate,
            java.time.LocalDate toDate,
            RecordStatus status
    );





    // =====================================================
    // Payment Based Search
    // =====================================================


	// Active receipt check ke liye
	boolean existsByPayment_IdAndStatusNot(Long paymentId, RecordStatus status);

	// Deleted receipt restore ke liye
	Optional<StudentFeeReceipt> findByPaymentIdAndStatus(Long paymentId, RecordStatus status);

	Optional<StudentFeeReceipt> findByPaymentIdAndStatusNot(Long paymentId, RecordStatus status);
	
	List<StudentFeeReceipt> findByStudentIdAndStatusNotOrderByReceiptDateDesc(
	        Long studentId,
	        RecordStatus status
	);
	
	long countByStudentId(Long studentId);

	long countByReceiptDateAndStatusNot(java.time.LocalDate receiptDate, RecordStatus status);


	long countByPrintedDateBetweenAndStatusNot(java.time.LocalDateTime fromDate, java.time.LocalDateTime toDate, RecordStatus status);

	List<StudentFeeReceipt> findTop10ByStatusNotOrderByReceiptDateDesc(RecordStatus status);

}