package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.SubjectRequestDto;
import com.sms.dto.SubjectResponseDto;
import com.sms.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    // =====================================================
    // DTO -> ENTITY
    // =====================================================

    // Primary Key
    @Mapping(target = "id", ignore = true)

    // Relationships
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "facultyMappings", ignore = true)
    

    // Auto Generated Fields
    @Mapping(target = "subjectCode", ignore = true)

    // Status
    @Mapping(target = "status", ignore = true)

    // BaseEntity Fields
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    // Audit Fields
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    Subject toEntity(
            SubjectRequestDto dto
    );





    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    @Mapping(
            source = "semester.id",
            target = "semesterId"
    )

    @Mapping(
            source = "semester.semesterName",
            target = "semesterName"
    )

    SubjectResponseDto toDto(
            Subject subject
    );




    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @BeanMapping(
            nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )

    // Primary Key
    @Mapping(target = "id", ignore = true)

    // Relationships
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "facultyMappings", ignore = true)

    // Auto Generated Fields
    @Mapping(target = "subjectCode", ignore = true)

    // Status
    @Mapping(target = "status", ignore = true)

    // BaseEntity Fields
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    // Audit Fields
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    void updateEntity(
            @MappingTarget Subject subject,
            SubjectRequestDto dto
    );

}