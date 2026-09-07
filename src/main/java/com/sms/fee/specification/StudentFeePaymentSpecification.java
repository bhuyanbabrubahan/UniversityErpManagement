package com.sms.fee.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.enums.PaymentMode;
import com.sms.fee.enums.PaymentStatus;

public final class StudentFeePaymentSpecification {

    private StudentFeePaymentSpecification() {
    }

    // =====================================================
    // MAIN SPECIFICATION
    // =====================================================
    
   

    public static Specification<StudentFeePayment> getSpecification(
            Long studentId,
            Long admissionId,
            String academicYear,
            PaymentStatus paymentStatus,
            PaymentMode paymentMode,
            BigDecimal minPaidAmount,
            BigDecimal maxPaidAmount,
            RecordStatus status) {

        Specification<StudentFeePayment> specification =
                Specification.where(null);

        // -------------------------------------------------
        // Default: exclude deleted records
        // -------------------------------------------------

        if (status == null) {

            specification = specification.and(
                    statusNot(RecordStatus.DELETED)
            );

        } else {

            specification = specification.and(
                    hasStatus(status)
            );
        }

        // -------------------------------------------------
        // Student
        // -------------------------------------------------

        if (studentId != null) {

            specification = specification.and(
                    hasStudentId(studentId)
            );
        }

        // -------------------------------------------------
        // Admission
        // -------------------------------------------------

        if (admissionId != null) {

            specification = specification.and(
                    hasAdmissionId(admissionId)
            );
        }

        // -------------------------------------------------
        // Academic Year
        // -------------------------------------------------

        if (academicYear != null && !academicYear.trim().isEmpty()) {

            specification = specification.and(
                    hasAcademicYear(academicYear.trim())
            );
        }

        // -------------------------------------------------
        // Payment Status
        // -------------------------------------------------

        if (paymentStatus != null) {

            specification = specification.and(
                    hasPaymentStatus(paymentStatus)
            );
        }

        // -------------------------------------------------
        // Payment Mode
        // -------------------------------------------------

        if (paymentMode != null) {

            specification = specification.and(
                    hasPaymentMode(paymentMode)
            );
        }

        // -------------------------------------------------
        // Minimum Paid Amount
        // -------------------------------------------------

        if (minPaidAmount != null) {

            specification = specification.and(
                    hasMinPaidAmount(minPaidAmount)
            );
        }

        // -------------------------------------------------
        // Maximum Paid Amount
        // -------------------------------------------------

        if (maxPaidAmount != null) {

            specification = specification.and(
                    hasMaxPaidAmount(maxPaidAmount)
            );
        }

        return specification;
    }

    // =====================================================
    // STUDENT
    // =====================================================

    private static Specification<StudentFeePayment> hasStudentId(
            Long studentId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("student").get("id"),
                        studentId
                );
    }

    // =====================================================
    // ADMISSION
    // =====================================================

    private static Specification<StudentFeePayment> hasAdmissionId(
            Long admissionId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("admission").get("id"),
                        admissionId
                );
    }

    // =====================================================
    // ACADEMIC YEAR
    // =====================================================

    private static Specification<StudentFeePayment> hasAcademicYear(
            String academicYear) {

        return (root, query, cb) ->
                cb.equal(
                        cb.trim(root.get("academicYear")),
                        academicYear
                );
    }

    // =====================================================
    // PAYMENT STATUS
    // =====================================================

    private static Specification<StudentFeePayment> hasPaymentStatus(
            PaymentStatus paymentStatus) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("paymentStatus"),
                        paymentStatus
                );
    }

    // =====================================================
    // PAYMENT MODE
    // =====================================================

    private static Specification<StudentFeePayment> hasPaymentMode(
            PaymentMode paymentMode) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("paymentMode"),
                        paymentMode
                );
    }

    // =====================================================
    // MINIMUM PAID AMOUNT
    // =====================================================

    private static Specification<StudentFeePayment> hasMinPaidAmount(
            BigDecimal minPaidAmount) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("paidAmount"),
                        minPaidAmount
                );
    }

    // =====================================================
    // MAXIMUM PAID AMOUNT
    // =====================================================

    private static Specification<StudentFeePayment> hasMaxPaidAmount(
            BigDecimal maxPaidAmount) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("paidAmount"),
                        maxPaidAmount
                );
    }

    // =====================================================
    // STATUS
    // =====================================================

    private static Specification<StudentFeePayment> hasStatus(
            RecordStatus status) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status
                );
    }

    // =====================================================
    // STATUS NOT
    // =====================================================

    private static Specification<StudentFeePayment> statusNot(
            RecordStatus status) {

        return (root, query, cb) ->
                cb.notEqual(
                        root.get("status"),
                        status
                );
    }
}

