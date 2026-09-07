package com.sms.audit.entity;


import java.time.LocalDateTime;

import com.sms.audit.enums.AuditAction;
import com.sms.enums.RecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private AuditAction action;

	@Column(nullable = false, length = 100)
	private String moduleName;

	@Column(nullable = false, length = 2000)
	private String description;

	@Column(nullable = false, length = 100)
	private String performedBy;

	@Column(nullable = false, length = 30)
	private String role;

	@Column(length = 50)
	private String ipAddress;

	@Column(length = 20)
	private String requestMethod;

	@Column(length = 300)
	private String requestUrl;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecordStatus status;

}
