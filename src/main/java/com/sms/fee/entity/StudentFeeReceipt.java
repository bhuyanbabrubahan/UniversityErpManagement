package com.sms.fee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;
import com.sms.scholarship.enums.ScholarshipType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@Table(
        name = "student_fee_receipts",

        indexes = {

                @Index(
                        name = "idx_receipt_payment",
                        columnList = "payment_id"
                ),

                @Index(
                        name = "idx_receipt_student",
                        columnList = "student_id"
                ),

                @Index(
                        name = "idx_receipt_admission",
                        columnList = "admission_id"
                ),

                @Index(
                        name = "idx_receipt_academic_year",
                        columnList = "academic_year"
                ),

                @Index(
                        name = "idx_receipt_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_receipt_date",
                        columnList = "receipt_date"
                ),

                @Index(
                        name = "idx_receipt_payment_status",
                        columnList = "payment_status"
                )
        },


        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_receipt_number",
                        columnNames = "receipt_number"
                ),

                @UniqueConstraint(
                        name = "uk_payment_receipt",
                        columnNames = "payment_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StudentFeeReceipt extends BaseEntity {



    // =====================================================
    // Primary Key
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // =====================================================
    // Receipt Information
    // =====================================================


    @Column(
            name = "receipt_number",
            nullable = false,
            length = 50,
            unique = true
    )
    private String receiptNumber;



    @Column(
            name = "receipt_date",
            nullable = false
    )
    private LocalDate receiptDate;



    @Column(
            length = 500
    )
    private String remarks;



    // =====================================================
    // Payment Relationship
    // =====================================================


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "payment_id",
            nullable = false
    )
    private StudentFeePayment payment;



    // =====================================================
    // Student Snapshot
    // =====================================================


    @Column(
            name = "student_id",
            nullable = false
    )
    private Long studentId;


    @Column(
            name = "student_name",
            nullable = false,
            length = 150
    )
    private String studentName;



    @Column(
            name = "roll_number",
            length = 50
    )
    private String rollNumber;



    // =====================================================
    // Admission Snapshot
    // =====================================================


    @Column(
            name = "admission_id",
            nullable = false
    )
    private Long admissionId;



    @Column(
            name = "admission_number",
            length = 50
    )
    private String admissionNumber;



    @Column(
            name = "academic_year",
            nullable = false,
            length = 20
    )
    private String academicYear;



    // =====================================================
    // Course Snapshot
    // =====================================================


    @Column(
            name = "course_id"
    )
    private Long courseId;



    @Column(
            name = "course_name",
            length = 150
    )
    private String courseName;



    // =====================================================
    // Department Snapshot
    // =====================================================


    @Column(
            name = "department_id"
    )
    private Long departmentId;



    @Column(
            name = "department_name",
            length = 150
    )
    private String departmentName;



    // =====================================================
    // Semester Snapshot
    // =====================================================


    @Column(
            name = "semester_id"
    )
    private Long semesterId;



    @Column(
            name = "semester_name",
            length = 100
    )
    private String semesterName;



	 // =====================================================
	 // Scholarship Snapshot
	 // =====================================================
	
	 @Column(
	         name = "scholarship_id"
	 )
	 private Long scholarshipId;
	
	 @Column(
	         name = "scholarship_name",
	         length = 100
	 )
	 private String scholarshipName;
	
	 @Enumerated(EnumType.STRING)
	 @Column(
	         name = "scholarship_type",
	         length = 30
	 )
	 private ScholarshipType scholarshipType;
	
	 @Column(
	         name = "scholarship_amount",
	         precision = 12,
	         scale = 2
	 )
	 private BigDecimal scholarshipAmount;



    // =====================================================
    // Fee Snapshot
    // =====================================================


    @Column(
            name = "total_fee",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalFee;



    @Column(
            name = "final_payable_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal finalPayableAmount;



    @Column(
            name = "paid_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal paidAmount;



    @Column(
            name = "due_amount",
            precision = 12,
            scale = 2,
            nullable = false
    )
    private BigDecimal dueAmount;



    // =====================================================
    // Payment Snapshot
    // =====================================================


    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_status",
            nullable = false,
            length = 20
    )
    private PaymentStatus paymentStatus;



    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_mode",
            length = 20
    )
    private PaymentMode paymentMode;



    @Column(
            name = "payment_date"
    )
    private LocalDate paymentDate;



    @Column(
            name = "transaction_reference",
            length = 100
    )
    private String transactionReference;



    // =====================================================
    // Record Status
    // =====================================================


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private RecordStatus status;
    
	 // Generate Information
    @Column(
            name = "generated_by"
    )
    private Long generatedBy;

    @Column(
            name = "generated_by_name",
            length = 100
    )
    private String generatedByName;
    
	
	
	 // =====================================================
	 // Print Information
	 // =====================================================
	
	 @Column(
	         name = "printed_by_user_id"
	 )
	 private Long printedByUserId;
	
	
	 @Column(
	         name = "printed_by",
	         length = 100
	 )
	 private String printedBy;
	
	
	 @Column(
	         name = "printed_date"
	 )
	 private LocalDateTime printedDate;
	
	
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