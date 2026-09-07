package com.sms.fee.report.paymentstatus.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportRequestDto;

import jakarta.persistence.criteria.Predicate;

public class StudentFeePaymentStatusReportSpecification {

	private StudentFeePaymentStatusReportSpecification() {

	}

	public static Specification<StudentFeePayment> search(PaymentStatusReportRequestDto request) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			// =====================================================
			// Student
			// =====================================================

			if (request.getStudentId() != null) {

				predicates.add(cb.equal(root.get("student").get("id"), request.getStudentId()));

			}

			if (request.getStudentName() != null && !request.getStudentName().isBlank()) {

				predicates.add(cb.like(cb.lower(root.get("student").get("fullName")),
						"%" + request.getStudentName().toLowerCase() + "%"));

			}

			if (request.getRollNumber() != null && !request.getRollNumber().isBlank()) {

				predicates.add(cb.like(cb.lower(root.get("student").get("rollNumber")),
						"%" + request.getRollNumber().toLowerCase() + "%"));

			}

			// =====================================================
			// Admission
			// =====================================================

			if (request.getAdmissionId() != null) {

				predicates.add(cb.equal(root.get("admission").get("id"), request.getAdmissionId()));

			}

			if (request.getAdmissionNumber() != null && !request.getAdmissionNumber().isBlank()) {

				predicates.add(cb.like(cb.lower(root.get("admission").get("admissionNumber")),
						"%" + request.getAdmissionNumber().toLowerCase() + "%"));

			}

			// =====================================================
			// Academic
			// =====================================================

			if (request.getAcademicYear() != null && !request.getAcademicYear().isBlank()) {

				predicates.add(cb.equal(root.get("academicYear"), request.getAcademicYear()));

			}

			if (request.getCourseId() != null) {

				predicates.add(cb.equal(root.get("admission").get("course").get("id"),
						request.getCourseId()));

			}

			if (request.getDepartmentId() != null) {

				predicates.add(cb.equal(root.get("admission").get("department").get("id"),
						request.getDepartmentId()));

			}

			if (request.getSemesterId() != null) {

				predicates.add(cb.equal(root.get("admission").get("semester").get("id"),
						request.getSemesterId()));

			}

			// =====================================================
			// Payment
			// =====================================================

			if (request.getPaymentStatus() != null) {

				predicates.add(cb.equal(root.get("paymentStatus"), request.getPaymentStatus()));

			}

			if (request.getPaymentMode() != null) {

				predicates.add(cb.equal(root.get("paymentMode"), request.getPaymentMode()));

			}

			// =====================================================
			// Paid Amount
			// =====================================================

			if (request.getMinPaidAmount() != null) {

				predicates.add(cb.greaterThanOrEqualTo(root.get("paidAmount"),
						request.getMinPaidAmount()));

			}

			if (request.getMaxPaidAmount() != null) {

				predicates.add(cb.lessThanOrEqualTo(root.get("paidAmount"),
						request.getMaxPaidAmount()));

			}

			// =====================================================
			// Due Amount
			// =====================================================

			if (request.getMinDueAmount() != null) {

				predicates.add(cb.greaterThanOrEqualTo(root.get("dueAmount"),
						request.getMinDueAmount()));

			}

			if (request.getMaxDueAmount() != null) {

				predicates.add(cb.lessThanOrEqualTo(root.get("dueAmount"),
						request.getMaxDueAmount()));

			}

			// =====================================================
			// Payment Date
			// =====================================================

			if (request.getPaymentDateFrom() != null) {

				predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"),
						request.getPaymentDateFrom()));

			}

			if (request.getPaymentDateTo() != null) {

				predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"),
						request.getPaymentDateTo()));

			}

			// =====================================================
			// Record Status
			// =====================================================

			if (request.getStatus() != null) {

				predicates.add(cb.equal(root.get("status"), request.getStatus()));

			} else {

				predicates.add(cb.notEqual(root.get("status"), RecordStatus.DELETED));

			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

	}

}