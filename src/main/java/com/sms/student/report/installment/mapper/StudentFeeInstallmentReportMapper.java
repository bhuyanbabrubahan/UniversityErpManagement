package com.sms.student.report.installment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sms.fee.installment.entity.StudentFeeInstallment;
import com.sms.student.report.installment.dto.StudentFeeInstallmentReportResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentFeeInstallmentReportMapper {

	@Mapping(target = "installmentId", source = "id")

	@Mapping(target = "paymentId", source = "payment.id")

	@Mapping(target = "studentId", source = "student.id")

	@Mapping(target = "admissionId", source = "admission.id")

	@Mapping(target = "admissionNumber", source = "admissionSnapshotNumber")

	@Mapping(target = "studentName", source = "studentName")

	@Mapping(target = "rollNumber", source = "rollNumber")

	@Mapping(target = "academicYear", source = "academicYear")

	@Mapping(target = "installmentNumber", source = "installmentNumber")

	@Mapping(target = "installmentSequence", source = "installmentSequence")

	@Mapping(target = "installmentAmount", source = "installmentAmount")

	@Mapping(target = "paidAmount", source = "paidAmount")
	@Mapping(target = "courseName", source = "admission.course.courseName")

	@Mapping(target = "departmentName", source = "admission.course.department.departmentName")
	@Mapping(target = "semesterName", source = "admission.semester.semesterName")

	@Mapping(target = "remainingAmount", source = "remainingAmount")

	@Mapping(target = "dueDate", source = "dueDate")

	@Mapping(target = "paymentDate", source = "paymentDate")

	@Mapping(target = "paymentMode", source = "paymentMode")

	@Mapping(target = "transactionReference", source = "transactionReference")

	@Mapping(target = "installmentStatus", source = "installmentStatus")

	@Mapping(target = "remarks", source = "remarks")

	StudentFeeInstallmentReportResponseDto toDto(StudentFeeInstallment entity);

}
