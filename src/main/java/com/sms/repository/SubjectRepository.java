package com.sms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.entity.Subject;
import com.sms.enums.RecordStatus;

public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {

	// Duplicate Subject Name in Same Semester
	boolean existsBySubjectNameAndSemesterId(String subjectName, Long semesterId);

	// Update Duplicate Check
	boolean existsBySubjectNameAndSemesterIdAndIdNot(String subjectName, Long semesterId, Long id);

	// Subject Code Duplicate
	boolean existsBySubjectCode(String subjectCode);

	// Soft Delete
	Optional<Subject> findByIdAndStatusNot(Long id, RecordStatus status);

	Page<Subject> findByStatusNot(RecordStatus status, Pageable pageable);

	// Auto Subject Code Generator
	Optional<Subject> findTopByOrderByIdDesc();
	
	
	//DASHBOARD
	long countByStatusNot(RecordStatus status);

}