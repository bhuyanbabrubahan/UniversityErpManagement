package com.sms.fee.report.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.admission.entity.Admission;
import com.sms.entity.Student;
import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.entity.StudentFeeReceipt;
import com.sms.fee.mapper.StudentFeePaymentMapper;
import com.sms.fee.mapper.StudentFeeReceiptMapper;
import com.sms.fee.refund.entity.StudentFeeRefund;
import com.sms.fee.refund.mapper.StudentFeeRefundMapper;
import com.sms.fee.report.dto.StudentPaymentHistoryResponseDto;
import com.sms.scholarship.entity.StudentScholarship;
import com.sms.scholarship.mapper.StudentScholarshipMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentPaymentHistoryReportMapper {

	private final StudentFeePaymentMapper paymentMapper;

	private final StudentFeeReceiptMapper receiptMapper;

	private final StudentFeeRefundMapper refundMapper;

	private final StudentScholarshipMapper scholarshipMapper;

	public StudentPaymentHistoryResponseDto toDto(

			Student student,

			Admission admission,

			List<StudentFeePayment> payments,

			List<StudentFeeReceipt> receipts,

			List<StudentFeeRefund> refunds,

			List<StudentScholarship> scholarships

	) {

		StudentPaymentHistoryResponseDto response = new StudentPaymentHistoryResponseDto();

		// Student basic information
		mapStudentInformation(response, student, admission);

		// Transaction history
		mapHistory(response, payments, receipts, refunds, scholarships);

		return response;
	}

	// =====================================================
	// Map Student Information
	// =====================================================

	private void mapStudentInformation(

			StudentPaymentHistoryResponseDto response,

			Student student,

			Admission admission

	) {

		response.setStudentId(student.getId());

		response.setStudentName(student.getFullName());

		response.setRollNumber(student.getRollNumber());

		response.setAdmissionId(admission.getId());

		response.setAdmissionNumber(admission.getAdmissionNumber());

		response.setDepartmentName(student.getDepartment().getDepartmentName());

		response.setCourseName(admission.getCourse().getCourseName());

	}

	// =====================================================
	// Map Transaction History
	// =====================================================

	private void mapHistory(

			StudentPaymentHistoryResponseDto response,

			List<StudentFeePayment> payments,

			List<StudentFeeReceipt> receipts,

			List<StudentFeeRefund> refunds,

			List<StudentScholarship> scholarships

	) {

		response.setPayments(

				payments.stream()

						.map(paymentMapper::toDto)

						.toList()

		);

		response.setReceipts(

				receipts.stream()

						.map(receiptMapper::toDto)

						.toList()

		);

		response.setRefunds(

				refunds.stream()

						.map(refundMapper::toDto)

						.toList()

		);

		response.setScholarships(

				scholarships.stream()

						.map(scholarshipMapper::toDto)

						.toList()

		);

	}

}