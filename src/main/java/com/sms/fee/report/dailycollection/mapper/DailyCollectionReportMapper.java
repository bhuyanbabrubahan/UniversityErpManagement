package com.sms.fee.report.dailycollection.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.entity.StudentFeePayment;
import com.sms.fee.report.dailycollection.dto.DailyCollectionReportResponseDto;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DailyCollectionReportMapper {

	// =====================================================
	// Entity -> Daily Collection Report DTO
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

	@Mapping(target = "paymentDate", source = "paymentDate")
	@Mapping(target = "paymentMode", source = "paymentMode")
	@Mapping(target = "paymentStatus", source = "paymentStatus")

	@Mapping(target = "paidAmount", source = "paidAmount")
	@Mapping(target = "dueAmount", source = "dueAmount")
	@Mapping(target = "finalPayableAmount", source = "finalPayableAmount")

	@Mapping(target = "transactionReference", source = "transactionReference")

	@Mapping(target = "academicYear", source = "academicYear")

	DailyCollectionReportResponseDto toDto(StudentFeePayment entity);

}