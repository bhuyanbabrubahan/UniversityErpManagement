package com.sms.student.report.installment.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportRequestDto;

import jakarta.persistence.criteria.Predicate;

public class StudentFeeInstallmentReportSpecification {

	private StudentFeeInstallmentReportSpecification() {
	}

	public static Specification<StudentFeeInstallment> search(StudentFeeInstallmentReportRequestDto request) {

		return (root, query, cb) -> {

			Predicate predicate = cb.notEqual(root.get("status"), RecordStatus.DELETED);

			// Student

			if (request.getStudentId() != null) {

				predicate = cb.and(predicate,
						cb.equal(root.get("student").get("id"), request.getStudentId()));

			}

			if (request.getStudentName() != null && !request.getStudentName().isBlank()) {

				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("studentName")),
								"%" + request.getStudentName().toLowerCase() + "%"));

			}

			if (request.getRollNumber() != null && !request.getRollNumber().isBlank()) {

				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("rollNumber")),
								"%" + request.getRollNumber().toLowerCase() + "%"));

			}

			// Admission

			if (request.getAdmissionId() != null) {

				predicate = cb.and(predicate,
						cb.equal(root.get("admission").get("id"), request.getAdmissionId()));

			}

			if (request.getAdmissionNumber() != null && !request.getAdmissionNumber().isBlank()) {

				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("admissionSnapshotNumber")),
								"%" + request.getAdmissionNumber().toLowerCase() + "%"));

			}

			// Academic Year

			if (request.getAcademicYear() != null && !request.getAcademicYear().isBlank()) {

				predicate = cb.and(predicate,
						cb.equal(root.get("academicYear"), request.getAcademicYear()));

			}

			// Installment

			if (request.getInstallmentStatus() != null) {

				predicate = cb.and(predicate,
						cb.equal(root.get("installmentStatus"), request.getInstallmentStatus()));

			}
			
			if (request.getPaymentId() != null) {

			    predicate = cb.and(
			            predicate,
			            cb.equal(
			                    root.get("payment").get("id"),
			                    request.getPaymentId()
			            )
			    );

			}

			if (request.getPaymentMode() != null) {

				predicate = cb.and(predicate,
						cb.equal(root.get("paymentMode"), request.getPaymentMode()));

			}

			// Amount

			if (request.getMinInstallmentAmount() != null) {

				predicate = cb.and(predicate,
						cb.greaterThanOrEqualTo(root.get("installmentAmount"),
								request.getMinInstallmentAmount()));

			}

			if (request.getMaxInstallmentAmount() != null) {

				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("installmentAmount"),
								request.getMaxInstallmentAmount()));

			}

			if (request.getMinPaidAmount() != null) {

				predicate = cb.and(predicate,
						cb.greaterThanOrEqualTo(root.get("paidAmount"),
								request.getMinPaidAmount()));

			}

			if (request.getMaxPaidAmount() != null) {

				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("paidAmount"),
								request.getMaxPaidAmount()));

			}

			if (request.getMinRemainingAmount() != null) {

				predicate = cb.and(predicate,
						cb.greaterThanOrEqualTo(root.get("remainingAmount"),
								request.getMinRemainingAmount()));

			}

			if (request.getMaxRemainingAmount() != null) {

				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("remainingAmount"),
								request.getMaxRemainingAmount()));

			}

			// Due Date

			if (request.getDueDateFrom() != null) {

				predicate = cb.and(predicate,
						cb.greaterThanOrEqualTo(root.get("dueDate"),
								request.getDueDateFrom()));

			}

			if (request.getDueDateTo() != null) {

				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("dueDate"),
								request.getDueDateTo()));

			}

			// Payment Date

			if (request.getPaymentDateFrom() != null) {

				predicate = cb.and(predicate,
						cb.greaterThanOrEqualTo(root.get("paymentDate"),
								request.getPaymentDateFrom()));

			}

			if (request.getPaymentDateTo() != null) {

				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("paymentDate"),
								request.getPaymentDateTo()));

			}

			return predicate;
		};

	}

}