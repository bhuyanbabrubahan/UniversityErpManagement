package com.sms.fee.installment.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.installment.dto.StudentFeeInstallmentSearchRequestDto;
import com.sms.fee.installment.entity.StudentFeeInstallment;

import jakarta.persistence.criteria.Predicate;

public class StudentFeeInstallmentSpecification {

    private StudentFeeInstallmentSpecification() {
    }

    public static Specification<StudentFeeInstallment> search(
            StudentFeeInstallmentSearchRequestDto request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // =====================================================
            // Soft Delete
            // =====================================================

            predicates.add(
                    cb.notEqual(
                            root.get("status"),
                            RecordStatus.DELETED
                    )
            );

            // =====================================================
            // Installment Number
            // =====================================================

            if (request.getInstallmentNumber() != null
                    && !request.getInstallmentNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("installmentNumber")),
                                "%" + request.getInstallmentNumber().toLowerCase() + "%"
                        )
                );
            }

            // =====================================================
            // Installment Sequence
            // =====================================================

            if (request.getInstallmentSequence() != null) {

                predicates.add(
                        cb.equal(
                                root.get("installmentSequence"),
                                request.getInstallmentSequence()
                        )
                );
            }

            // =====================================================
            // Student Name
            // =====================================================

            if (request.getStudentName() != null
                    && !request.getStudentName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("studentName")),
                                "%" + request.getStudentName().toLowerCase() + "%"
                        )
                );
            }

            // =====================================================
            // Roll Number
            // =====================================================

            if (request.getRollNumber() != null
                    && !request.getRollNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("rollNumber")),
                                "%" + request.getRollNumber().toLowerCase() + "%"
                        )
                );
            }

            // =====================================================
            // Admission Number
            // =====================================================

            if (request.getAdmissionSnapshotNumber() != null
                    && !request.getAdmissionSnapshotNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("admissionSnapshotNumber")),
                                "%" + request.getAdmissionSnapshotNumber().toLowerCase() + "%"
                        )
                );
            }

            // =====================================================
            // Academic Year
            // =====================================================

            if (request.getAcademicYear() != null
                    && !request.getAcademicYear().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("academicYear"),
                                request.getAcademicYear()
                        )
                );
            }

            // =====================================================
            // Payment Id
            // =====================================================

            if (request.getPaymentId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("payment").get("id"),
                                request.getPaymentId()
                        )
                );
            }

            // =====================================================
            // Installment Status
            // =====================================================

            if (request.getInstallmentStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("installmentStatus"),
                                request.getInstallmentStatus()
                        )
                );
            }

            // =====================================================
            // Payment Mode
            // =====================================================

            if (request.getPaymentMode() != null) {

                predicates.add(
                        cb.equal(
                                root.get("paymentMode"),
                                request.getPaymentMode()
                        )
                );
            }

            // =====================================================
            // Installment Amount Range
            // =====================================================

            if (request.getMinInstallmentAmount() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("installmentAmount"),
                                request.getMinInstallmentAmount()
                        )
                );
            }

            if (request.getMaxInstallmentAmount() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("installmentAmount"),
                                request.getMaxInstallmentAmount()
                        )
                );
            }

            // =====================================================
            // Due Date Range
            // =====================================================

            if (request.getDueDateFrom() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("dueDate"),
                                request.getDueDateFrom()
                        )
                );
            }

            if (request.getDueDateTo() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("dueDate"),
                                request.getDueDateTo()
                        )
                );
            }

            // =====================================================
            // Payment Date Range
            // =====================================================

            if (request.getPaymentDateFrom() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("paymentDate"),
                                request.getPaymentDateFrom()
                        )
                );
            }

            if (request.getPaymentDateTo() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("paymentDate"),
                                request.getPaymentDateTo()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }

}