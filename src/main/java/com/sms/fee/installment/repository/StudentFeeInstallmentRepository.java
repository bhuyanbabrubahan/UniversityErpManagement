package com.sms.fee.installment.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.fee.installment.enums.InstallmentStatus;

public interface StudentFeeInstallmentRepository
        extends JpaRepository<StudentFeeInstallment, Long>,
        JpaSpecificationExecutor<StudentFeeInstallment> {

    // =====================================================
    // Find By Id
    // =====================================================

    Optional<StudentFeeInstallment> findByIdAndStatusNot(
            Long id,
            RecordStatus status
    );

    // =====================================================
    // Get All Active
    // =====================================================

    Page<StudentFeeInstallment> findByStatusNot(
            RecordStatus status,
            Pageable pageable
    );

    // =====================================================
    // Duplicate Sequence Check
    // =====================================================

    boolean existsByPayment_IdAndInstallmentSequenceAndStatusNot(
            Long paymentId,
            Integer installmentSequence,
            RecordStatus status
    );

    // =====================================================
    // Duplicate Sequence Check For Update
    // =====================================================

    boolean existsByPayment_IdAndInstallmentSequenceAndStatusNotAndIdNot(
            Long paymentId,
            Integer installmentSequence,
            RecordStatus status,
            Long id
    );

    // =====================================================
    // Dashboard Counts
    // =====================================================

    long countByStatusNot(
            RecordStatus status
    );

    long countByInstallmentStatusAndStatusNot(
            InstallmentStatus installmentStatus,
            RecordStatus status
    );
    
    @Query("""
            SELECT COUNT(i)
            FROM StudentFeeInstallment i
            WHERE i.dueDate < :today
            AND i.installmentStatus = :installmentStatus
            AND i.status <> :status
            """)
    long countOverdue(
            @Param("today") LocalDate today,
            @Param("installmentStatus") InstallmentStatus installmentStatus,
            @Param("status") RecordStatus status
    );



    // =====================================================
    // Total Installment Amount
    // =====================================================

    @Query("""
            SELECT COALESCE(SUM(i.installmentAmount),0)
            FROM StudentFeeInstallment i
            WHERE i.status <> :status
            """)
    BigDecimal getTotalInstallmentAmount(
            @Param("status")
            RecordStatus status
    );
    
    
    
    
    
    @Query("""
            SELECT COALESCE(SUM(i.installmentAmount), 0)
            FROM StudentFeeInstallment i
            WHERE i.payment.id = :paymentId
            AND i.status <> :status
            AND (:installmentId IS NULL OR i.id <> :installmentId)
            """)
    BigDecimal getTotalInstallmentAmountByPayment(
            @Param("paymentId") Long paymentId,

            @Param("status") RecordStatus status,

            @Param("installmentId") Long installmentId
    );
    
    

    // =====================================================
    // Total Paid Amount
    // =====================================================

    @Query("""
            SELECT COALESCE(SUM(i.paidAmount),0)
            FROM StudentFeeInstallment i
            WHERE i.status <> :status
            """)
    BigDecimal getTotalPaidAmount(
            @Param("status")
            RecordStatus status
    );

    // =====================================================
    // Total Remaining Amount
    // =====================================================

    @Query("""
            SELECT COALESCE(SUM(i.remainingAmount),0)
            FROM StudentFeeInstallment i
            WHERE i.status <> :status
            """)
    BigDecimal getTotalRemainingAmount(
            @Param("status")
            RecordStatus status
    );

    // =====================================================
    // Overdue Installments
    // =====================================================

    @Query("""
            SELECT i
            FROM StudentFeeInstallment i
            WHERE i.dueDate < :today
            AND i.installmentStatus = :installmentStatus
            AND i.status <> :status
            """)
    Page<StudentFeeInstallment> findOverdueInstallments(
            @Param("today")
            LocalDate today,

            @Param("installmentStatus")
            InstallmentStatus installmentStatus,

            @Param("status")
            RecordStatus status,

            Pageable pageable
    );
    
    long countByStudent(Student student);

    long countByStudent_Id(Long studentId);

}