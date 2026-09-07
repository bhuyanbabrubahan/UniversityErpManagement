package com.sms.fee.report.specification;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportRequestDto;

import jakarta.persistence.criteria.Predicate;

public class StudentFeeLedgerReportSpecification {

	private StudentFeeLedgerReportSpecification() {
	}

	// =====================================================
	// Ledger Report Search
	// =====================================================

	public static Specification<StudentFeePayment> search(
			StudentFeeLedgerReportRequestDto request) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			// =====================================================
			// Student Filters
			// =====================================================

			if (request.getStudentId() != null) {

				predicates.add(cb.equal(root.get("student").get("id"),
						request.getStudentId()));

			}

			if (request.getStudentName() != null
					&& !request.getStudentName().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("student").get("fullName")),

						"%" + request.getStudentName().toLowerCase() + "%"

				));

			}

			if (request.getRollNumber() != null
					&& !request.getRollNumber().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("student").get("rollNumber")),

						"%" + request.getRollNumber().toLowerCase() + "%"

				));

			}

			// =====================================================
			// Admission Filters
			// =====================================================

			if (request.getAdmissionId() != null) {

				predicates.add(cb.equal(root.get("admission").get("id"),
						request.getAdmissionId()));

			}

			if (request.getAdmissionNumber() != null
					&& !request.getAdmissionNumber().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("admission").get("admissionNumber")),

						"%" + request.getAdmissionNumber().toLowerCase() + "%"

				));

			}

			// =====================================================
			// Academic Filters
			// =====================================================

			if (request.getAcademicYear() != null
					&& !request.getAcademicYear().isBlank()) {

				predicates.add(cb.equal(root.get("academicYear"),
						request.getAcademicYear()));

			}

			if (request.getCourseName() != null
					&& !request.getCourseName().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("admission").get("course").get("courseName")),

						"%" + request.getCourseName().toLowerCase() + "%"

				));

			}

			if (request.getDepartmentName() != null
					&& !request.getDepartmentName().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("admission").get("department").get("departmentName")),

						"%" + request.getDepartmentName().toLowerCase() + "%"

				));

			}

			if (request.getSemesterName() != null
					&& !request.getSemesterName().isBlank()) {

				predicates.add(cb.like(

						cb.lower(root.get("admission").get("semester").get("semesterName")),

						"%" + request.getSemesterName().toLowerCase() + "%"

				));

			}

			// =====================================================
			// Payment Filters
			// =====================================================

			if (request.getPaymentStatus() != null) {

				predicates.add(cb.equal(root.get("paymentStatus"),
						request.getPaymentStatus()));

			}

			if (request.getPaymentDateFrom() != null) {

				predicates.add(cb.greaterThanOrEqualTo(

						root.get("paymentDate"),

						request.getPaymentDateFrom()

				));

			}

			if (request.getPaymentDateTo() != null) {

				predicates.add(cb.lessThanOrEqualTo(

						root.get("paymentDate"),

						request.getPaymentDateTo()

				));

			}

			// =====================================================
			// Record Status
			// =====================================================

			if (request.getStatus() != null) {

				predicates.add(cb.equal(root.get("status"),
						request.getStatus()));

			}

			return cb.and(predicates.toArray(new Predicate[0]));

		};

	}

}