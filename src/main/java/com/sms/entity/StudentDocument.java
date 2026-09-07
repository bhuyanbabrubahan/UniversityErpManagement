package com.sms.entity;

import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.DocumentStatus;
import com.sms.enums.DocumentType;
import com.sms.enums.RecordStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocument extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Which student uploaded document

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DocumentType documentType;

	@Column(nullable = false)
	private String originalFileName;

	@Column(nullable = false, unique = true)
	private String storedFileName;

	private String filePath;

	private String contentType;

	private Long fileSize;

	private LocalDateTime uploadedAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecordStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "document_status", nullable = false)
	private DocumentStatus documentStatus;

	// =====================================
	// Document Verification Audit
	// =====================================

	@Column(name = "reviewed_by_user_id")
	private Long reviewedByUserId;

	@Column(name = "reviewed_by")
	private String reviewedBy;

	@Column(name = "reviewed_at")
	private LocalDateTime reviewedAt;

}