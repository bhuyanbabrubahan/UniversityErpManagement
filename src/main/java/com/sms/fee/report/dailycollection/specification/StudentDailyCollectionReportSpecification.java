package com.sms.fee.report.dailycollection.specification;


import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.sms.enums.RecordStatus;
import com.sms.fee.entity.StudentFeePayment;


public class StudentDailyCollectionReportSpecification {

	private StudentDailyCollectionReportSpecification() {
	}

	// =====================================================
	// Daily Collection Report Search
	// =====================================================

	public static Specification<StudentFeePayment> search(LocalDate paymentDate) {

		return (root, query, cb) -> {

			var predicate = cb.conjunction();

			// ============================================
			// Exclude Deleted Records
			// ============================================

			predicate.getExpressions().add(

					cb.notEqual(root.get("status"), RecordStatus.DELETED)

			);

			// ============================================
			// Payment Date
			// ============================================

			if (paymentDate != null) {

				predicate.getExpressions().add(

						cb.equal(root.get("paymentDate"), paymentDate)

				);

			}

			return predicate;

		};

	}

}