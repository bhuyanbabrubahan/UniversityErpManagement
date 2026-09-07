package com.sms.fee.mapper;

import java.math.BigDecimal;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.fee.dto.StudentFeePaymentRequestDto;
import com.sms.fee.dto.StudentFeePaymentResponseDto;
import com.sms.fee.entity.StudentFeePayment;

@Mapper(componentModel = "spring")
public interface StudentFeePaymentMapper {

	// =====================================================
	// DTO -> ENTITY
	// =====================================================

	@Mapping(target = "id", ignore = true)

	@Mapping(target = "student", ignore = true)

	@Mapping(target = "admission", ignore = true)

	@Mapping(target = "scholarship", ignore = true)
	@Mapping(target = "scholarshipAmount", ignore = true)
	@Mapping(target = "scholarshipName", ignore = true)
	@Mapping(target = "scholarshipType", ignore = true)

	@Mapping(target = "totalFee", ignore = true)

	@Mapping(target = "finalPayableAmount", ignore = true)

	@Mapping(target = "dueAmount", ignore = true)

	@Mapping(target = "paymentStatus", ignore = true)

	@Mapping(target = "status", ignore = true)

	@Mapping(target = "createdAt", ignore = true)

	@Mapping(target = "updatedAt", ignore = true)

	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "createdBy", ignore = true)

	@Mapping(target = "updatedByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	
	@Mapping(target = "deletedByUserId", ignore = true)
	@Mapping(target = "deletedBy", ignore = true)
	@Mapping(target = "deletedDate", ignore = true)

	StudentFeePayment toEntity(StudentFeePaymentRequestDto dto);

	// =====================================================
	// ENTITY -> DTO
	// =====================================================

	@Mapping(target = "studentId", source = "student.id")

	@Mapping(target = "studentName", expression = """
			java(
			    entity.getStudent() != null
			        ? entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName()
			        : null
			)
			""")

	@Mapping(target = "admissionId", source = "admission.id")

	// scholarshipId handled in AfterMapping

	@Mapping(target = "scholarshipId", ignore = true)

	// calculated runtime fields

	@Mapping(target = "scholarshipAmount", ignore = true)

	StudentFeePaymentResponseDto toDto(StudentFeePayment entity);

	// =====================================================
	// UPDATE ENTITY
	// =====================================================

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

	@Mapping(target = "id", ignore = true)

	@Mapping(target = "student", ignore = true)

	@Mapping(target = "admission", ignore = true)

	@Mapping(target = "scholarship", ignore = true)

	@Mapping(target = "totalFee", ignore = true)

	@Mapping(target = "dueAmount", ignore = true)

	@Mapping(target = "paymentStatus", ignore = true)

	@Mapping(target = "status", ignore = true)

	@Mapping(target = "finalPayableAmount", ignore = true)

	@Mapping(target = "createdAt", ignore = true)

	@Mapping(target = "updatedAt", ignore = true)

	// scholarshipId handled in AfterMapping

	// calculated runtime fields

	@Mapping(target = "scholarshipAmount", ignore = true)
	@Mapping(target = "scholarshipName", ignore = true)
	@Mapping(target = "scholarshipType", ignore = true)

	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "createdBy", ignore = true)

	@Mapping(target = "updatedByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	
	@Mapping(target = "deletedByUserId", ignore = true)
	@Mapping(target = "deletedBy", ignore = true)
	@Mapping(target = "deletedDate", ignore = true)

	void updateEntity(@MappingTarget StudentFeePayment payment, StudentFeePaymentRequestDto dto);

	// =====================================================
	// RESPONSE CALCULATION
	// =====================================================

	@AfterMapping
	default void calculateScholarshipFields(StudentFeePayment entity, @MappingTarget StudentFeePaymentResponseDto dto) {

		BigDecimal scholarshipAmount = BigDecimal.ZERO;

		if (entity.getScholarship() != null) {

			dto.setScholarshipId(entity.getScholarship().getId());

			if (entity.getScholarship().getApprovedAmount() != null) {

				scholarshipAmount = entity.getScholarship().getApprovedAmount();

			}

		} else {

			dto.setScholarshipId(null);

		}

		dto.setScholarshipAmount(scholarshipAmount);

	}

}