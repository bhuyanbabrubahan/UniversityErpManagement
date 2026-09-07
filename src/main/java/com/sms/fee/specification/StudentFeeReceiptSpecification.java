package com.sms.fee.specification;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

import jakarta.persistence.criteria.Predicate;



public final class StudentFeeReceiptSpecification {



    private StudentFeeReceiptSpecification() {

    }





    public static Specification<StudentFeeReceipt> getSpecification(

            String receiptNumber,

            Long paymentId,

            Long studentId,
            String studentName,
            String rollNumber,

            Long admissionId,
            String admissionNumber,
            String academicYear,

            String courseName,
            String departmentName,
            String semesterName,

            Long scholarshipId,

            PaymentStatus paymentStatus,
            PaymentMode paymentMode,

            BigDecimal minPaidAmount,
            BigDecimal maxPaidAmount,

            BigDecimal minTotalFee,
            BigDecimal maxTotalFee,

            BigDecimal minDueAmount,
            BigDecimal maxDueAmount,

            BigDecimal minFinalPayableAmount,
            BigDecimal maxFinalPayableAmount,

            LocalDate receiptDateFrom,
            LocalDate receiptDateTo,

            RecordStatus status
    ) {



    	return (root, query, cb) -> {

    	    List<Predicate> predicates = new ArrayList<>();

    	    // =====================================================
    	    // Receipt Number
    	    // =====================================================

    	    if (receiptNumber != null && !receiptNumber.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("receiptNumber")),
    	                        "%" + receiptNumber.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Payment Id
    	    // =====================================================

    	    if (paymentId != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("payment").get("id"),
    	                        paymentId
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Student
    	    // =====================================================

    	    if (studentId != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("studentId"),
    	                        studentId
    	                )
    	        );
    	    }

    	    if (studentName != null && !studentName.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("studentName")),
    	                        "%" + studentName.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    if (rollNumber != null && !rollNumber.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("rollNumber")),
    	                        "%" + rollNumber.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Admission
    	    // =====================================================

    	    if (admissionId != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("admissionId"),
    	                        admissionId
    	                )
    	        );
    	    }

    	    if (admissionNumber != null && !admissionNumber.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("admissionNumber")),
    	                        "%" + admissionNumber.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    if (academicYear != null && !academicYear.isBlank()) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("academicYear"),
    	                        academicYear
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Course
    	    // =====================================================

    	    if (courseName != null && !courseName.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("courseName")),
    	                        "%" + courseName.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Department
    	    // =====================================================

    	    if (departmentName != null && !departmentName.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("departmentName")),
    	                        "%" + departmentName.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Semester
    	    // =====================================================

    	    if (semesterName != null && !semesterName.isBlank()) {

    	        predicates.add(
    	                cb.like(
    	                        cb.lower(root.get("semesterName")),
    	                        "%" + semesterName.toLowerCase() + "%"
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Scholarship
    	    // =====================================================

    	    if (scholarshipId != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("scholarshipId"),
    	                        scholarshipId
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Payment
    	    // =====================================================

    	    if (paymentStatus != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("paymentStatus"),
    	                        paymentStatus
    	                )
    	        );
    	    }

    	    if (paymentMode != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("paymentMode"),
    	                        paymentMode
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Paid Amount
    	    // =====================================================

    	    if (minPaidAmount != null) {

    	        predicates.add(
    	                cb.greaterThanOrEqualTo(
    	                        root.get("paidAmount"),
    	                        minPaidAmount
    	                )
    	        );
    	    }

    	    if (maxPaidAmount != null) {

    	        predicates.add(
    	                cb.lessThanOrEqualTo(
    	                        root.get("paidAmount"),
    	                        maxPaidAmount
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Total Fee
    	    // =====================================================

    	    if (minTotalFee != null) {

    	        predicates.add(
    	                cb.greaterThanOrEqualTo(
    	                        root.get("totalFee"),
    	                        minTotalFee
    	                )
    	        );
    	    }

    	    if (maxTotalFee != null) {

    	        predicates.add(
    	                cb.lessThanOrEqualTo(
    	                        root.get("totalFee"),
    	                        maxTotalFee
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Due Amount
    	    // =====================================================

    	    if (minDueAmount != null) {

    	        predicates.add(
    	                cb.greaterThanOrEqualTo(
    	                        root.get("dueAmount"),
    	                        minDueAmount
    	                )
    	        );
    	    }

    	    if (maxDueAmount != null) {

    	        predicates.add(
    	                cb.lessThanOrEqualTo(
    	                        root.get("dueAmount"),
    	                        maxDueAmount
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Final Payable Amount
    	    // =====================================================

    	    if (minFinalPayableAmount != null) {

    	        predicates.add(
    	                cb.greaterThanOrEqualTo(
    	                        root.get("finalPayableAmount"),
    	                        minFinalPayableAmount
    	                )
    	        );
    	    }

    	    if (maxFinalPayableAmount != null) {

    	        predicates.add(
    	                cb.lessThanOrEqualTo(
    	                        root.get("finalPayableAmount"),
    	                        maxFinalPayableAmount
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Receipt Date
    	    // =====================================================

    	    if (receiptDateFrom != null) {

    	        predicates.add(
    	                cb.greaterThanOrEqualTo(
    	                        root.get("receiptDate"),
    	                        receiptDateFrom
    	                )
    	        );
    	    }

    	    if (receiptDateTo != null) {

    	        predicates.add(
    	                cb.lessThanOrEqualTo(
    	                        root.get("receiptDate"),
    	                        receiptDateTo
    	                )
    	        );
    	    }

    	    // =====================================================
    	    // Record Status
    	    // =====================================================

    	    if (status != null) {

    	        predicates.add(
    	                cb.equal(
    	                        root.get("status"),
    	                        status
    	                )
    	        );

    	    } else {

    	        predicates.add(
    	                cb.notEqual(
    	                        root.get("status"),
    	                        RecordStatus.DELETED
    	                )
    	        );
    	    }

    	    return cb.and(
    	            predicates.toArray(new Predicate[0])
    	    );
    	};


    }


}