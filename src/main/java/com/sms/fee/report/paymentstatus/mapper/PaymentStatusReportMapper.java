package com.sms.fee.report.paymentstatus.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.paymentstatus.dto.PaymentStatusReportResponseDto;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentStatusReportMapper {

	@Mapping(target = "paymentId", source = "id")

	// =====================================================
	// Student
	// =====================================================

	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName", source = "student.fullName")
	@Mapping(target = "rollNumber", source = "student.rollNumber")

	// =====================================================
	// Admission
	// =====================================================

	@Mapping(target = "admissionId", source = "admission.id")
	@Mapping(target = "admissionNumber", source = "admission.admissionNumber")

	// =====================================================
	// Academic
	// =====================================================

	@Mapping(target = "courseName", source = "admission.course.courseName")
	@Mapping(target = "departmentName", source = "admission.department.departmentName")
	@Mapping(target = "semesterName", source = "admission.semester.semesterName")

	// =====================================================
	// Scholarship
	// =====================================================

	@Mapping(target = "scholarshipAmount", source = "scholarship.approvedAmount")

	PaymentStatusReportResponseDto toDto(StudentFeePayment entity);

}