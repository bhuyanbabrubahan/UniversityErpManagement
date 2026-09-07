package com.sms.fee.installment.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.admission.entity.Admission;
import com.sms.common.entity.BaseEntity;
import com.sms.entity.Student;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.installment.enums.InstallmentStatus;

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
@Table(name = "student_fee_installments")
@Getter
@Setter
public class StudentFeeInstallment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// =====================================================
	// Installment Details
	// =====================================================

	@Column(nullable = false, unique = true, length = 50)
	private String installmentNumber;

	@Column(nullable = false)
	private Integer installmentSequence;

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
	// Fee Payment
	// =====================================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", nullable = false)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private StudentFeePayment payment;

	// =====================================================
	// Snapshot
	// =====================================================

	@Column(name = "student_snapshot_id", nullable = false)
	private Long studentSnapshotId;

	@Column(nullable = false, length = 150)
	private String studentName;

	@Column(nullable = false, length = 50)
	private String rollNumber;

	@Column(name = "admission_snapshot_id", nullable = false)
	private Long admissionSnapshotId;

	@Column(name = "admission_number_snapshot", length = 50)
	private String admissionSnapshotNumber;

	@Column(nullable = false, length = 20)
	private String academicYear;

	// =====================================================
	// Amount
	// =====================================================

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal installmentAmount;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal paidAmount;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal remainingAmount;

	// =====================================================
	// Dates
	// =====================================================

	@Column(nullable = false)
	private LocalDate dueDate;

	private LocalDate paymentDate;

	// =====================================================
	// Payment
	// =====================================================

	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;

	@Column(length = 100)
	private String transactionReference;

	// =====================================================
	// Remarks
	// =====================================================

	@Column(length = 500)
	private String remarks;

	// =====================================================
	// Status
	// =====================================================

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InstallmentStatus installmentStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RecordStatus status;

	
	// =====================================================
	// Completion Information
	// =====================================================

	@Column(
	        name = "completed_by_user_id"
	)
	private Long completedByUserId;


	@Column(
	        name = "completed_by",
	        length = 100
	)
	private String completedBy;


	@Column(
	        name = "completed_date"
	)
	private LocalDateTime completedDate;
	
	// =====================================================
	// Delete Information
	// =====================================================

	@Column(
	        name = "deleted_by_user_id"
	)
	private Long deletedByUserId;


	@Column(
	        name = "deleted_by",
	        length = 100
	)
	private String deletedBy;


	@Column(
	        name = "deleted_date"
	)
	private LocalDateTime deletedDate;

}