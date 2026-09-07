package com.sms.scholarship.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.admission.entity.Admission;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.scholarship.enums.ScholarshipStatus;
import com.sms.scholarship.enums.ScholarshipType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_scholarships")
public class StudentScholarship extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", nullable = false)
	private Admission admission;

	@Column(name = "scholarship_name", nullable = false, length = 100)
	private String scholarshipName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScholarshipType scholarshipType;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal scholarshipValue;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal requestedAmount;

	@Column(nullable = false, length = 20)
	private String academicYear;

	@Column(length = 500)
	private String remarks;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ScholarshipStatus scholarshipStatus;

	@Enumerated(EnumType.STRING)
	private RecordStatus status;


	// =====================================================
	// Approval Information
	// =====================================================
	@Column(precision = 10, scale = 2)
	private BigDecimal approvedAmount;

	@Column(name = "approved_by_user_id")
	private Long approvedByUserId;

	@Column(name = "approved_by", length = 100)
	private String approvedBy;

	@Column(name = "approval_date")
	private LocalDateTime approvalDate;

	// =====================================================
	// Rejection Information
	// =====================================================
	@Column(name = "rejection_reason", length = 500)
	private String rejectionReason;

	@Column(name = "rejected_by_user_id")
	private Long rejectedByUserId;

	@Column(name = "rejected_by", length = 100)
	private String rejectedBy;

	@Column(name = "rejected_date")
	private LocalDateTime rejectedDate;
	
	// =====================================================
	// Delete Information
	// =====================================================

	@Column(name = "deleted_by_user_id")
	private Long deletedByUserId;

	@Column(name = "deleted_by", length = 100)
	private String deletedBy;

	@Column(name = "deleted_date")
	private LocalDateTime deletedDate;

}
