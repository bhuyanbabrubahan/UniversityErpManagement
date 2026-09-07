package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.FacultySubjectRequestDto;
import com.sms.dto.FacultySubjectResponseDto;
import com.sms.entity.FacultySubjectMapping;

@Mapper(componentModel = "spring")
public interface FacultySubjectMapper {

    // =====================================================
    // DTO -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    // Relationships
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "subject", ignore = true)

    // Students
    @Mapping(target = "students", ignore = true)

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

    FacultySubjectMapping toEntity(
            FacultySubjectRequestDto dto
    );


    // =====================================================
    // ENTITY -> RESPONSE DTO
    // =====================================================

    @Mapping(source = "faculty.id", target = "facultyId")

    @Mapping(
            expression = "java(entity.getFaculty().getFirstName() + \" \" + entity.getFaculty().getLastName())",
            target = "facultyName"
    )

    @Mapping(source = "subject.id", target = "subjectId")

    @Mapping(source = "subject.subjectName", target = "subjectName")

    FacultySubjectResponseDto toDto(
            FacultySubjectMapping entity
    );


    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )

    @Mapping(target = "id", ignore = true)

    // Relationships
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "subject", ignore = true)

    // Students
    @Mapping(target = "students", ignore = true)

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
            @MappingTarget FacultySubjectMapping entity,
            FacultySubjectRequestDto dto
    );
}
