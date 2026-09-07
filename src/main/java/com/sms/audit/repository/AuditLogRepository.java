package com.sms.audit.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sms.audit.entity.AuditLog;
import com.sms.audit.enums.AuditAction;
import com.sms.enums.RecordStatus;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {


	// Find active audit log by id
	Optional<AuditLog> findByIdAndStatus(
			Long id,
			RecordStatus status
	);


	// Get all active audit logs
	Page<AuditLog> findByStatus(
			RecordStatus status,
			Pageable pageable
	);


	// Find audit logs by action
	Page<AuditLog> findByActionAndStatus(
			AuditAction action,
			RecordStatus status,
			Pageable pageable
	);


	// Find audit logs by user
	Page<AuditLog> findByPerformedByAndStatus(
			String performedBy,
			RecordStatus status,
			Pageable pageable
	);


	// Find audit logs between date range
	Page<AuditLog> findByTimestampBetweenAndStatus(
			LocalDateTime from,
			LocalDateTime to,
			RecordStatus status,
			Pageable pageable
	);


	// Count active logs
	long countByStatus(
			RecordStatus status
	);

}