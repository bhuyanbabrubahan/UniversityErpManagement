package com.sms.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sms.audit.dto.AuditLogResponseDto;
import com.sms.audit.dto.AuditLogSearchRequestDto;

public interface AuditLogService {

	/**
	 * Save audit log
	 */
	AuditLogResponseDto createAuditLog(AuditLogResponseDto request);

	/**
	 * Get audit log by id
	 */
	AuditLogResponseDto getAuditLogById(Long id);

	/**
	 * Get all audit logs
	 */
	Page<AuditLogResponseDto> getAllAuditLogs(Pageable pageable);

	/**
	 * Dynamic search
	 */
	Page<AuditLogResponseDto> searchAuditLogs(AuditLogSearchRequestDto request, Pageable pageable);

	/**
	 * Count active audit logs
	 */
	long countAuditLogs();

}