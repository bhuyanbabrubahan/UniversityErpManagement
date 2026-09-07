package com.sms.fee.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.fee.dto.FeeStructureRequestDto;
import com.sms.fee.dto.FeeStructureResponseDto;
import com.sms.fee.entity.FeeStructure;

@Mapper(componentModel = "spring")
public interface FeeStructureMapper {

	// ==========================================================
	// DTO -> ENTITY
	// ==========================================================

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "semester", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	FeeStructure toEntity(FeeStructureRequestDto dto);

	// ==========================================================
	// ENTITY -> DTO
	// ==========================================================

	@Mapping(source = "course.id", target = "courseId")
	@Mapping(source = "course.courseName", target = "courseName")

	@Mapping(source = "semester.id", target = "semesterId")
	@Mapping(source = "semester.semesterName", target = "semesterName")

	// createdAt & updatedAt automatically mapped
	FeeStructureResponseDto toDto(FeeStructure entity);

	// ==========================================================
	// UPDATE ENTITY
	// ==========================================================

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	@Mapping(target = "semester", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void updateEntity(@MappingTarget FeeStructure entity, FeeStructureRequestDto dto);

}