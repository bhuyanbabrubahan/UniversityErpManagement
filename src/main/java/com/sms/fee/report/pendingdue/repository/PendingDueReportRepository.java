package com.sms.fee.report.pendingdue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.pendingdue.dto.PendingDueReportResponseDto;

public interface PendingDueReportRepository extends JpaRepository<StudentFeePayment, Long> {

	// =====================================================
	// Pending Due Report
	// =====================================================

	@Query("""
				        SELECT new com.sms.fee.report.pendingdue.dto.PendingDueReportResponseDto(

			    s.id,

			    CONCAT(s.firstName, ' ', s.lastName),

			    s.rollNumber,

			    a.admissionNumber,

			    a.academicYear,

			    d.departmentName,

			    c.courseName,

			    p.totalFee,

			    p.scholarshipAmount,

			    p.finalPayableAmount,

			    p.paidAmount,

			    p.dueAmount,
			    
			    p.paymentStatus,
			    
			    p.status

			)


				        FROM StudentFeePayment p


				        JOIN p.student s

				        JOIN p.admission a

				        JOIN a.course c

				        JOIN c.department d


				        WHERE p.status <> com.sms.enums.RecordStatus.DELETED

				        AND p.dueAmount > 0


				        ORDER BY p.dueAmount DESC

				        """)
	Page<PendingDueReportResponseDto> getPendingDueReport(Pageable pageable);

}