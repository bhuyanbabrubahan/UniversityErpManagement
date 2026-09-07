package com.sms.fee.report.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.dto.report.FeeReportSearchRequestDto;
import com.sms.fee.entity.StudentFeeReceipt;

import jakarta.persistence.criteria.Predicate;

public final class FeeReportSpecification {
	
	

    private FeeReportSpecification() {
    }

    public static Specification<StudentFeeReceipt> build(
            FeeReportSearchRequestDto request) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            // Active Records
            predicate = cb.and(
                    predicate,
                    cb.notEqual(root.get("status"), RecordStatus.DELETED)
            );

            // Receipt Number
            if (hasText(request.getReceiptNumber())) {
                predicate = cb.and(predicate,
                        cb.like(
                                cb.lower(root.get("receiptNumber")),
                                "%" + request.getReceiptNumber().trim().toLowerCase() + "%"));
            }

            // Academic Year
            if (hasText(request.getAcademicYear())) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("academicYear"),
                                request.getAcademicYear().trim()));
            }

            // Department
            if (request.getDepartmentId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("departmentId"),
                                request.getDepartmentId()));
            }

            // Course
            if (request.getCourseId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("courseId"),
                                request.getCourseId()));
            }


            // Student
            if (request.getStudentId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("studentId"),
                                request.getStudentId()));
            }

            // Payment Status
            if (request.getPaymentStatus() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("paymentStatus"),
                                request.getPaymentStatus()));
            }

            // Payment Mode
            if (request.getPaymentMode() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("paymentMode"),
                                request.getPaymentMode()));
            }

            // Transaction Reference
            if (hasText(request.getTransactionReference())) {
                predicate = cb.and(predicate,
                        cb.like(
                                cb.lower(root.get("transactionReference")),
                                "%" + request.getTransactionReference().trim().toLowerCase() + "%"));
            }

            // From Date
            if (request.getFromDate() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("paymentDate"),
                                request.getFromDate()));
            }

            // To Date
            if (request.getToDate() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(
                                root.get("paymentDate"),
                                request.getToDate()));
            }

            return predicate;
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}