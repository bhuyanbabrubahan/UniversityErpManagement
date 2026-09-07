package com.sms.fee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.admission.entity.Admission;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;
import com.sms.scholarship.entity.StudentScholarship;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student_fee_payments")
@Getter
@Setter
public class StudentFeePayment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// =====================================================
	// Student
	// =====================================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Student student;

	// =====================================================
	// Admission
	// =====================================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Admission admission;

	// =====================================================
	// Academic Details
	// =====================================================

	@Column(nullable = false, length = 20)
	private String academicYear;

	// =====================================================
	// Fee Details
	// =====================================================

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal totalFee;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal paidAmount;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal dueAmount;

	// =====================================================
	// Payment
	// =====================================================

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus paymentStatus;

	@Column(nullable = false)
	private LocalDate paymentDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMode paymentMode;

	@Column(length = 100)
	private String transactionReference;

	@Column(length = 500)
	private String remarks;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal finalPayableAmount;

	// =====================================================
	// Scholarship Reference
	// =====================================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scholarship_id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private StudentScholarship scholarship;

	// =====================================================
	// Scholarship Snapshot
	// =====================================================

	@Column(name = "scholarship_name", length = 100)
	private String scholarshipName;

	@Enumerated(EnumType.STRING)
	@Column(name = "scholarship_type", length = 30)
	private ScholarshipType scholarshipType;

	@Column(name = "scholarship_amount", precision = 12, scale = 2, nullable = false)
	private BigDecimal scholarshipAmount;

	// =====================================================
	// Status
	// =====================================================

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecordStatus status;

	
	
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