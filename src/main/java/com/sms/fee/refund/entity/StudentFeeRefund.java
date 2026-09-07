package com.sms.fee.refund.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sms.common.entity.BaseEntity;
import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.refund.enums.RefundMode;
import com.sms.fee.refund.enums.RefundStatus;

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
import jakarta.persistence.ManyToOne;
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
        name = "student_fee_refunds",

        indexes = {


                @Index(
                        name = "idx_refund_payment",
                        columnList = "payment_id"
                ),


                @Index(
                        name = "idx_refund_receipt",
                        columnList = "receipt_id"
                ),


                @Index(
                        name = "idx_refund_student",
                        columnList = "student_id"
                ),


                @Index(
                        name = "idx_refund_admission",
                        columnList = "admission_id"
                ),


                @Index(
                        name = "idx_refund_status",
                        columnList = "refund_status"
                ),


                @Index(
                        name = "idx_refund_date",
                        columnList = "refund_date"
                )

        },


        uniqueConstraints = {


                @UniqueConstraint(
                        name = "uk_refund_number",
                        columnNames = "refund_number"
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

public class StudentFeeRefund extends BaseEntity {



    // =====================================================
    // Primary Key
    // =====================================================


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;





    // =====================================================
    // Refund Information
    // =====================================================


    @Column(
            name = "refund_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String refundNumber;



    @Column(
            name = "refund_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal refundAmount;



    @Column(
            name = "refund_reason",
            length = 500
    )
    private String refundReason;



    @Column(
            name = "refund_date"
    )
    private LocalDate refundDate;





    // =====================================================
    // Payment Relationship
    // =====================================================


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "payment_id",
            nullable = false
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StudentFeePayment payment;





    // =====================================================
    // Receipt Relationship
    // =====================================================


    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "receipt_id"
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StudentFeeReceipt receipt;

    // =====================================================
    // Receipt Snapshot
    // =====================================================
    @Column(
            name = "receipt_number",
            length = 50
    )
    private String receiptNumber;


    @Column(
            name = "receipt_date"
    )
    private LocalDate receiptDate;

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
            name = "admission_id"
    )
    private Long admissionId;



    @Column(
            name = "admission_number",
            length = 50
    )
    private String admissionNumber;





    // =====================================================
    // Refund Mode
    // =====================================================


    @Enumerated(EnumType.STRING)
    @Column(
            name = "refund_mode",
            length = 20
    )
    private RefundMode refundMode;





    // =====================================================
    // Transaction Details
    // =====================================================


    @Column(
            name = "payment_transaction_reference",
            length = 100
    )
    private String paymentTransactionReference;



    @Column(
            length = 500
    )
    private String remarks;



    // =====================================================
    // Refund Status
    // =====================================================


    @Enumerated(EnumType.STRING)
    @Column(
            name = "refund_status",
            nullable = false,
            length = 20
    )
    private RefundStatus refundStatus;


	 // =====================================================
	 // Request Information
	 // =====================================================
	
	 @Column(
	         name = "requested_by_user_id"
	 )
	 private Long requestedByUserId;
	
	
	 @Column(
	         name = "requested_by",
	         length = 100
	 )
	 private String requestedBy;
	
	
	 @Column(
	         name = "requested_date"
	 )
	 private LocalDateTime requestedDate;


    // =====================================================
    // Record Status
    // =====================================================


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private RecordStatus status;

    
	    
	 // =====================================================
	 // Approval Information
	 // =====================================================
	
    @Column(
            name = "approved_by_user_id"
    )
    private Long approvedByUserId;


    @Column(
            name = "approved_by",
            length = 100
    )
    private String approvedBy;


    @Column(
            name = "approved_date"
    )
    private LocalDateTime approvedDate;
	
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
	 // Rejection Information
	 // =====================================================
	
    @Column(
            name = "rejected_by_user_id"
    )
    private Long rejectedByUserId;


    @Column(
            name = "rejected_by",
            length = 100
    )
    private String rejectedBy;


    @Column(
            name = "rejected_date"
    )
    private LocalDateTime rejectedDate;


    @Column(
            name = "rejection_reason",
            length = 500
    )
    private String rejectionReason;
	
	 // =====================================================
	 // Cancellation Information
	 // =====================================================
	
    @Column(
            name = "cancelled_by_user_id"
    )
    private Long cancelledByUserId;


    @Column(
            name = "cancelled_by",
            length = 100
    )
    private String cancelledBy;


    @Column(
            name = "cancelled_date"
    )
    private LocalDateTime cancelledDate;


    @Column(
            name = "cancellation_reason",
            length = 500
    )
    private String cancellationReason;
    
    
	 // =====================================================
	 // Delete Information
	 // =====================================================
	
	 @Column(name = "deleted_by_user_id")
	 private Long deletedByUserId;
	
	
	 @Column(name = "deleted_by", length = 100)
	 private String deletedBy;
	
	
	 @Column(name = "deleted_date")
	 private LocalDateTime deletedDate;
	
	 // =====================================================
	 // Bank Transaction
	 // =====================================================
	
    @Column(
            name = "refund_transaction_reference",
            length = 100
    )
    private String refundTransactionReference;
    
	 // =====================================================
	 // Bank / Gateway Refund Details
	 // =====================================================
	
	 @Column(
	         name = "bank_reference",
	         length = 100
	 )
	 private String bankReference;



}