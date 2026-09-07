package com.sms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.entity.Faculty;
import com.sms.enums.RecordStatus;

public interface FacultyRepository extends JpaRepository<Faculty, Long>, JpaSpecificationExecutor<Faculty> {

	Optional<Faculty> findByIdAndStatus(Long id, RecordStatus status);

	boolean existsByEmployeeCode(String employeeCode);

	Page<Faculty> findByStatus(RecordStatus status, Pageable pageable);

	Optional<Faculty> findByUserIdAndStatus(Long userId, RecordStatus status);

	// DASHBOARD
	long countByStatusNot(RecordStatus status);

	long countByStatus(RecordStatus status);

	Optional<Faculty> findByIdAndStatusNot(Long id, RecordStatus status);

	Page<Faculty> findByStatusNot(RecordStatus status, Pageable pageable);
	
	

}