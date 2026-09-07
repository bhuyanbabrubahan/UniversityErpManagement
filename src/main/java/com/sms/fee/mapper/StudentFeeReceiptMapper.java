package com.sms.fee.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.fee.dto.StudentFeeReceiptRequestDto;
import com.sms.fee.dto.StudentFeeReceiptResponseDto;
import com.sms.fee.entity.StudentFeeReceipt;

@Mapper(componentModel = "spring")
public interface StudentFeeReceiptMapper {

	// =====================================================
	// Entity -> Response DTO
	// =====================================================

	@Mapping(target = "paymentId", source = "payment.id")

	StudentFeeReceiptResponseDto toDto(StudentFeeReceipt entity);

	// =====================================================
	// Request DTO -> Entity
	// =====================================================

	@Mapping(target = "id", ignore = true)

	@Mapping(target = "payment", ignore = true)

	@Mapping(target = "receiptNumber", ignore = true)
	@Mapping(target = "receiptDate", ignore = true)
	@Mapping(target = "remarks", ignore = true)

	@Mapping(target = "status", ignore = true)

	// Snapshot Fields

	@Mapping(target = "studentId", ignore = true)
	@Mapping(target = "studentName", ignore = true)
	@Mapping(target = "rollNumber", ignore = true)

	@Mapping(target = "admissionId", ignore = true)
	@Mapping(target = "admissionNumber", ignore = true)

	@Mapping(target = "academicYear", ignore = true)

	@Mapping(target = "courseId", ignore = true)
	@Mapping(target = "courseName", ignore = true)

	@Mapping(target = "departmentId", ignore = true)
	@Mapping(target = "departmentName", ignore = true)

	@Mapping(target = "semesterId", ignore = true)
	@Mapping(target = "semesterName", ignore = true)

	@Mapping(target = "scholarshipId", ignore = true)
	@Mapping(target = "scholarshipAmount", ignore = true)
	@Mapping(target = "scholarshipName", ignore = true)
	@Mapping(target = "scholarshipType", ignore = true)

	@Mapping(target = "totalFee", ignore = true)
	@Mapping(target = "finalPayableAmount", ignore = true)
	@Mapping(target = "paidAmount", ignore = true)
	@Mapping(target = "dueAmount", ignore = true)

	@Mapping(target = "paymentStatus", ignore = true)
	@Mapping(target = "paymentMode", ignore = true)
	@Mapping(target = "paymentDate", ignore = true)
	@Mapping(target = "transactionReference", ignore = true)

	// Base Entity

	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	// Audit

	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "createdBy", ignore = true)

	// Print Audit

	@Mapping(target = "printedByUserId", ignore = true)
	@Mapping(target = "printedBy", ignore = true)
	@Mapping(target = "printedDate", ignore = true)

	// Delete Audit

	@Mapping(target = "deletedByUserId", ignore = true)
	@Mapping(target = "deletedBy", ignore = true)
	@Mapping(target = "deletedDate", ignore = true)

	@Mapping(target = "generatedBy", ignore = true)
	@Mapping(target = "generatedByName", ignore = true)

	StudentFeeReceipt toEntity(StudentFeeReceiptRequestDto dto);

	// =====================================================
	// Update Entity
	// =====================================================

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

	@Mapping(target = "id", ignore = true)

	@Mapping(target = "payment", ignore = true)


	@Mapping(target = "receiptNumber", ignore = true)
	@Mapping(target = "receiptDate", ignore = true)
	@Mapping(target = "remarks", ignore = true)

	@Mapping(target = "status", ignore = true)

	// Snapshot Fields

	@Mapping(target = "studentId", ignore = true)
	@Mapping(target = "studentName", ignore = true)
	@Mapping(target = "rollNumber", ignore = true)

	@Mapping(target = "admissionId", ignore = true)
	@Mapping(target = "admissionNumber", ignore = true)

	@Mapping(target = "academicYear", ignore = true)

	@Mapping(target = "courseId", ignore = true)
	@Mapping(target = "courseName", ignore = true)

	@Mapping(target = "departmentId", ignore = true)
	@Mapping(target = "departmentName", ignore = true)

	@Mapping(target = "semesterId", ignore = true)
	@Mapping(target = "semesterName", ignore = true)

	@Mapping(target = "scholarshipId", ignore = true)
	@Mapping(target = "scholarshipAmount", ignore = true)
	@Mapping(target = "scholarshipName", ignore = true)
	@Mapping(target = "scholarshipType", ignore = true)

	@Mapping(target = "totalFee", ignore = true)
	@Mapping(target = "finalPayableAmount", ignore = true)
	@Mapping(target = "paidAmount", ignore = true)
	@Mapping(target = "dueAmount", ignore = true)

	@Mapping(target = "paymentStatus", ignore = true)
	@Mapping(target = "paymentMode", ignore = true)
	@Mapping(target = "paymentDate", ignore = true)
	@Mapping(target = "transactionReference", ignore = true)

	// Base Entity

	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	// Audit

	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "createdBy", ignore = true)

	// Print Audit

	@Mapping(target = "printedByUserId", ignore = true)
	@Mapping(target = "printedBy", ignore = true)
	@Mapping(target = "printedDate", ignore = true)

	// Delete Audit

	@Mapping(target = "deletedByUserId", ignore = true)
	@Mapping(target = "deletedBy", ignore = true)
	@Mapping(target = "deletedDate", ignore = true)

	@Mapping(target = "generatedBy", ignore = true)
	@Mapping(target = "generatedByName", ignore = true)

	void updateEntity(@MappingTarget StudentFeeReceipt entity, StudentFeeReceiptRequestDto dto);

}