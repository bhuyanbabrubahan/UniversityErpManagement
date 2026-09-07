package com.sms.fee.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

public interface StudentFeePaymentRepository
		extends JpaRepository<StudentFeePayment, Long>, JpaSpecificationExecutor<StudentFeePayment> {

	// =====================================================
	// Find By Id (Soft Delete Support)
	// =====================================================

	Optional<StudentFeePayment> findByIdAndStatusNot(Long id, RecordStatus status);

	// =====================================================
	// Get All Active Records
	// =====================================================

	Page<StudentFeePayment> findByStatusNot(RecordStatus status, Pageable pageable);

	// =====================================================
	// Duplicate Payment Check
	// =====================================================

	boolean existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNot(Long studentId, Long admissionId,
			String academicYear, RecordStatus status);
	
	
	boolean existsByStudentIdAndAdmissionIdAndAcademicYearAndStatusNotAndIdNot(
	        Long studentId,
	        Long admissionId,
	        String academicYear,
	        RecordStatus status,
	        Long id
	);
	
	
	boolean existsByScholarshipIdAndStatusNot(
	        Long scholarshipId,
	        RecordStatus status
	);

	List<StudentFeePayment> findByStudentIdAndStatusNotOrderByPaymentDateDesc(
	        Long studentId,
	        RecordStatus status
	);
	
	List<StudentFeePayment> findByStudentIdAndStatusNotOrderByCreatedAtDesc(
	        Long studentId,
	        RecordStatus status
	);
	
	long countByStudent(Student student);

    long countByStudent_Id(Long studentId);
	
	List<StudentFeePayment> findTop50ByStatusNotOrderByDueAmountDesc(RecordStatus status);
	List<StudentFeePayment> findTop10ByStatusNotOrderByPaymentDateDesc(RecordStatus status);

	@Query("SELECT COUNT(DISTINCT p.student.id) FROM StudentFeePayment p WHERE p.status <> :status")
	long countDistinctStudents(@Param("status") RecordStatus status);

	@Query("SELECT COUNT(DISTINCT p.student.id) FROM StudentFeePayment p WHERE p.paymentStatus = :paymentStatus AND p.status <> :status")
	long countDistinctStudentsByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus, @Param("status") RecordStatus status);


	// =====================================================
	// Fee Dashboard
	// =====================================================

	// Total Active Payments
	long countByStatusNot(
	        RecordStatus status
	);

	// Total Payments By Status
	long countByPaymentStatusAndStatusNot(
	        PaymentStatus paymentStatus,
	        RecordStatus status
	);

	// Total Fee Collection
	@Query("""
	       SELECT COALESCE(SUM(p.paidAmount),0)
	       FROM StudentFeePayment p
	       WHERE p.status <> :status
	       """)
	BigDecimal getTotalFeeCollection(
	        @Param("status")
	        RecordStatus status
	);

	// Today's Collection
	@Query("""
	       SELECT COALESCE(SUM(p.paidAmount),0)
	       FROM StudentFeePayment p
	       WHERE p.paymentDate = :paymentDate
	       AND p.status <> :status
	       """)
	BigDecimal getTodayCollection(
	        @Param("paymentDate")
	        LocalDate paymentDate,

	        @Param("status")
	        RecordStatus status
	);

	// Current Month Collection
	@Query("""
	       SELECT COALESCE(SUM(p.paidAmount),0)
	       FROM StudentFeePayment p
	       WHERE YEAR(p.paymentDate)=:year
	       AND MONTH(p.paymentDate)=:month
	       AND p.status <> :status
	       """)
	BigDecimal getMonthlyCollection(
	        @Param("year")
	        int year,

	        @Param("month")
	        int month,

	        @Param("status")
	        RecordStatus status
	);

	// Total Pending Due
	@Query("""
	       SELECT COALESCE(SUM(p.dueAmount),0)
	       FROM StudentFeePayment p
	       WHERE p.status <> :status
	       """)
	BigDecimal getTotalPendingDue(
	        @Param("status")
	        RecordStatus status
	);

	// Collection By Payment Mode
	@Query("""
	       SELECT COALESCE(SUM(p.paidAmount),0)
	       FROM StudentFeePayment p
	       WHERE p.paymentMode=:paymentMode
	       AND p.status <> :status
	       """)
	BigDecimal getCollectionByPaymentMode(
	        @Param("paymentMode")
	        PaymentMode paymentMode,

	        @Param("status")
	        RecordStatus status
	);
	
	// Dashboard - Total Payable Fee

	@Query("""
	       SELECT COALESCE(SUM(p.totalFee),0)
	       FROM StudentFeePayment p
	       WHERE p.status <> :status
	       """)
	BigDecimal getTotalPayableFee(
	        @Param("status") RecordStatus status
	);
	
	
	@Query("""
			SELECT p
			FROM StudentFeePayment p
			LEFT JOIN FETCH p.scholarship
			WHERE p.id = :id
			AND p.status <> :status
			""")
			Optional<StudentFeePayment> findByIdWithScholarship(
			        @Param("id") Long id,
			        @Param("status") RecordStatus status
			);

	// =====================================================
	// Student Dashboard Fee Summary
	// =====================================================

	@Query("""
	        SELECT COALESCE(SUM(p.totalFee),0)
	        FROM StudentFeePayment p
	        WHERE p.student.id = :studentId
	        AND p.status <> :status
	        """)
	BigDecimal getStudentTotalFee(
	        @Param("studentId") Long studentId,
	        @Param("status") RecordStatus status
	);


	@Query("""
	        SELECT COALESCE(SUM(p.paidAmount),0)
	        FROM StudentFeePayment p
	        WHERE p.student.id = :studentId
	        AND p.status <> :status
	        """)
	BigDecimal getStudentPaidFee(
	        @Param("studentId") Long studentId,
	        @Param("status") RecordStatus status
	);


	@Query("""
	        SELECT COALESCE(SUM(p.dueAmount),0)
	        FROM StudentFeePayment p
	        WHERE p.student.id = :studentId
	        AND p.status <> :status
	        """)
	BigDecimal getStudentPendingFee(
	        @Param("studentId") Long studentId,
	        @Param("status") RecordStatus status
	);
	
	
	
}