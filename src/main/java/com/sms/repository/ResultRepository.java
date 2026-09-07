package com.sms.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sms.entity.Result;
import com.sms.enums.RecordStatus;
import com.sms.enums.ResultStatus;

public interface ResultRepository extends JpaRepository<Result, Long>, JpaSpecificationExecutor<Result> {

	// ==============================
	// Get Active Result By Id
	// ==============================

	Optional<Result> findByIdAndStatus(Long id, RecordStatus status);

	
	// ==============================
	// Pagination
	// Active / Deleted Result Filter
	// ==============================

	Page<Result> findByStatus(RecordStatus status, Pageable pageable);
	
	// ==============================
	// Duplicate Result Check
	// Student + Exam Combination
	// ==============================
	
	 boolean existsByStudentIdAndExamIdAndStatus(
	            Long studentId,
	            Long examId,
	            RecordStatus status
	    );

	//DASHBOARD
	 long countByStatusNot(RecordStatus status);

	 long countByResultStatusAndStatusNot(
	         ResultStatus resultStatus,
	         RecordStatus status
	 );

	 @Query("""
	        SELECT AVG(r.percentage)
	        FROM Result r
	        WHERE r.status <> :status
	        """)
	 BigDecimal findAveragePercentage(
	         @Param("status")
	         RecordStatus status
	 );
	 
	 @Query("""
			 SELECT COUNT(r)
			 FROM Result r
			 JOIN r.exam e
			 JOIN e.subject s
			 JOIN s.facultyMappings fsm
			 WHERE fsm.faculty.id = :facultyId
			 AND r.status <> :status
			 """)
			 long countByFaculty(
			         @Param("facultyId") Long facultyId,
			         @Param("status") RecordStatus status
			 );
	 
	 @Query("""
			 SELECT COUNT(r)
			 FROM Result r
			 WHERE r.exam.subject.id IN
			 (
			     SELECT fsm.subject.id
			     FROM FacultySubjectMapping fsm
			     WHERE fsm.faculty.id = :facultyId
			 )
			 AND r.status <> :status
			 """)
			 long countByFacultyIdAndStatusNot(
			         @Param("facultyId") Long facultyId,
			         @Param("status") RecordStatus status
			 );
	 
	 long countByStudentIdAndStatusNot(
		        Long studentId,
		        RecordStatus status
		);
	 
}