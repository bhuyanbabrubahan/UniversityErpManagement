package com.sms.report.refund.specification;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.report.refund.dto.RefundReportRequestDto;

import jakarta.persistence.criteria.Predicate;


public class StudentFeeRefundReportSpecification {


	private StudentFeeRefundReportSpecification() {
	}


	public static Specification<StudentFeeRefund> search(
			RefundReportRequestDto request) {


		return (root, query, cb) -> {


			Predicate predicate =
					cb.notEqual(
							root.get("status"),
							RecordStatus.DELETED
					);



			// Student Name

			if (request.getStudentName() != null
					&& !request.getStudentName().isBlank()) {


				predicate = cb.and(
						predicate,
						cb.like(
								cb.lower(root.get("studentName")),
								"%" + request.getStudentName().toLowerCase() + "%"
						)
				);

			}



			// Roll Number

			if (request.getRollNumber() != null
					&& !request.getRollNumber().isBlank()) {


				predicate = cb.and(
						predicate,
						cb.like(
								cb.lower(root.get("rollNumber")),
								"%" + request.getRollNumber().toLowerCase() + "%"
						)
				);

			}



			// Admission Number

			if (request.getAdmissionNumber() != null
					&& !request.getAdmissionNumber().isBlank()) {


				predicate = cb.and(
						predicate,
						cb.like(
								cb.lower(root.get("admissionNumber")),
								"%" + request.getAdmissionNumber().toLowerCase() + "%"
						)
				);

			}


			// Receipt Date Filter

			if (request.getReceiptDateFrom() != null) {

				predicate = cb.and(
						predicate,
						cb.greaterThanOrEqualTo(
								root.get("receiptDate"),
								request.getReceiptDateFrom()
						)
				);

			}


			if (request.getReceiptDateTo() != null) {

				predicate = cb.and(
						predicate,
						cb.lessThanOrEqualTo(
								root.get("receiptDate"),
								request.getReceiptDateTo()
						)
				);

			}

			// Receipt Number

			if (request.getReceiptNumber() != null
					&& !request.getReceiptNumber().isBlank()) {


				predicate = cb.and(
						predicate,
						cb.like(
								cb.lower(root.get("receiptNumber")),
								"%" + request.getReceiptNumber().toLowerCase() + "%"
						)
				);

			}




			// Refund Number

			if (request.getRefundNumber() != null
					&& !request.getRefundNumber().isBlank()) {


				predicate = cb.and(
						predicate,
						cb.like(
								cb.lower(root.get("refundNumber")),
								"%" + request.getRefundNumber().toLowerCase() + "%"
						)
				);

			}




			// Refund Status

			if (request.getRefundStatus() != null) {


				predicate = cb.and(
						predicate,
						cb.equal(
								root.get("refundStatus"),
								request.getRefundStatus()
						)
				);

			}




			// Refund Amount Range

			if (request.getMinRefundAmount() != null) {


				predicate = cb.and(
						predicate,
						cb.greaterThanOrEqualTo(
								root.get("refundAmount"),
								request.getMinRefundAmount()
						)
				);

			}



			if (request.getMaxRefundAmount() != null) {


				predicate = cb.and(
						predicate,
						cb.lessThanOrEqualTo(
								root.get("refundAmount"),
								request.getMaxRefundAmount()
						)
				);

			}





			// Refund Date Range

			if (request.getRefundDateFrom() != null) {


				predicate = cb.and(
						predicate,
						cb.greaterThanOrEqualTo(
								root.get("refundDate"),
								request.getRefundDateFrom()
						)
				);

			}



			if (request.getRefundDateTo() != null) {


				predicate = cb.and(
						predicate,
						cb.lessThanOrEqualTo(
								root.get("refundDate"),
								request.getRefundDateTo()
						)
				);

			}





			// Status

			if (request.getStatus() != null) {


				predicate = cb.and(
						predicate,
						cb.equal(
								root.get("status"),
								request.getStatus()
						)
				);

			}



			return predicate;

		};

	}

}