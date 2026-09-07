package com.admission.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.admission.entity.Admission;
import com.admission.enums.AdmissionStatus;
import com.sms.enums.RecordStatus;

public interface AdmissionRepository extends JpaRepository<Admission, Long>, JpaSpecificationExecutor<Admission> {

	// Find admission by id excluding cancelled admission
	Optional<Admission> findByIdAndAdmissionStatusNot(Long id, AdmissionStatus admissionStatus);

	// Find admission by id and specific status
	Optional<Admission> findByIdAndAdmissionStatus(Long id, AdmissionStatus admissionStatus);

	// Check duplicate active admission for student
	// One student can have only one ACTIVE admission
	boolean existsByStudent_IdAndAdmissionStatus(Long studentId, AdmissionStatus admissionStatus);

	// Pagination with status filtering excluding cancelled admission
	// Pagination with status filtering
	Page<Admission> findByAdmissionStatusNotAndStatus(
	        AdmissionStatus admissionStatus,
	        RecordStatus status,
	        Pageable pageable
	);
	
	
	boolean existsByStudent_IdAndAdmissionStatusAndStatus(
	        Long studentId,
	        AdmissionStatus admissionStatus,
	        RecordStatus status
	);
	
	boolean existsByStudent_IdAndAdmissionStatusAndStatusAndIdNot(
	        Long studentId,
	        AdmissionStatus admissionStatus,
	        RecordStatus status,
	        Long id
	);




	// Find admission by admission number
	Optional<Admission> findByAdmissionNumber(String admissionNumber);

	// Check duplicate admission number for generator safety
	boolean existsByAdmissionNumber(String admissionNumber);

	// Dashboard count of all admissions
	long count();

	// Dashboard count by admission status
	long countByAdmissionStatus(AdmissionStatus admissionStatus);

	Optional<Admission> findByStudentIdAndStatusNot(
	        Long studentId,
	        RecordStatus status
	);
	
	Optional<Admission> findByStudentId(Long studentId);
	
	Optional<Admission> findByStudentIdAndStatus(Long studentId, RecordStatus status);
	
	
	
	// =====================================================
	// DATA AUDIT
	// =====================================================

	@Query("""
	        SELECT a
	        FROM Admission a
	        LEFT JOIN FETCH a.student
	        LEFT JOIN FETCH a.department
	        LEFT JOIN FETCH a.course
	        LEFT JOIN FETCH a.semester
	        ORDER BY a.id
	        """)
	List<Admission> findAllForDataAudit();


	@Query("""
	        SELECT a
	        FROM Admission a
	        LEFT JOIN FETCH a.student
	        LEFT JOIN FETCH a.department
	        LEFT JOIN FETCH a.course
	        LEFT JOIN FETCH a.semester
	        WHERE a.student.id = :studentId
	        ORDER BY a.admissionDate DESC, a.id DESC
	        """)
	List<Admission> findAllByStudentIdForDataAudit(
	        @Param("studentId") Long studentId
	);
	
	List<Admission> findAllByStudentId(Long studentId);

}