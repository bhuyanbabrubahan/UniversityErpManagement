package com.sms.fee.report.ledger.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.ledger.dto.StudentFeeLedgerReportResponseDto;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentFeeLedgerReportMapper {

	// =====================================================
	// Entity -> Ledger Report DTO
	// =====================================================

	@Mapping(target = "paymentId", source = "id")

	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName", source = "student.fullName")
	@Mapping(target = "rollNumber", source = "student.rollNumber")

	@Mapping(target = "admissionId", source = "admission.id")
	@Mapping(target = "admissionNumber", source = "admission.admissionNumber")

	@Mapping(target = "courseName", source = "admission.course.courseName")
	@Mapping(target = "departmentName", source = "admission.department.departmentName")
	@Mapping(target = "semesterName", source = "admission.semester.semesterName")

	@Mapping(target = "academicYear", source = "academicYear")

	@Mapping(target = "totalFee", source = "totalFee")
	@Mapping(target = "finalPayableAmount", source = "finalPayableAmount")
	@Mapping(target = "paidAmount", source = "paidAmount")
	@Mapping(target = "dueAmount", source = "dueAmount")

	@Mapping(target = "paymentDate", source = "paymentDate")
	@Mapping(target = "paymentMode", source = "paymentMode")
	@Mapping(target = "paymentStatus", source = "paymentStatus")
	@Mapping(target = "transactionReference", source = "transactionReference")

	StudentFeeLedgerReportResponseDto toDto(StudentFeePayment entity);

}