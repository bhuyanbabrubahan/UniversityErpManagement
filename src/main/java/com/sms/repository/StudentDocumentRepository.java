package com.sms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.entity.StudentDocument;
import com.sms.enums.DocumentType;
import com.sms.enums.RecordStatus;

public interface StudentDocumentRepository
		extends JpaRepository<StudentDocument, Long>, JpaSpecificationExecutor<StudentDocument> {


	Optional<StudentDocument> findByIdAndStatus(Long id, RecordStatus status);


	Optional<StudentDocument> findByStudentIdAndDocumentTypeAndStatus(Long studentId, DocumentType documentType,
			RecordStatus status);


	boolean existsByStudentIdAndDocumentTypeAndStatus(Long studentId, DocumentType documentType,
			RecordStatus status);


	Page<StudentDocument> findByStudentIdAndStatus(Long studentId, RecordStatus status, Pageable pageable);

	long countByStudent_Id(Long studentId);
}