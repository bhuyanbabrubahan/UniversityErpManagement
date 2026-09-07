package com.sms.erp.curriculum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sms.erp.curriculum.dto.CurriculumRequestDto;
import com.sms.erp.curriculum.dto.CurriculumResponseDto;
import com.sms.erp.curriculum.entity.Curriculum;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CurriculumMapper {

    // =====================================================
    // REQUEST -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)



    Curriculum toEntity(
            CurriculumRequestDto request
    );


    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    @Mapping(target = "id", source = "id")

    @Mapping(target = "code", source = "code")
    @Mapping(target = "academicYear", source = "academicYear")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "semester", source = "semester")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "credits", source = "credits")
    @Mapping(target = "contactHours", source = "contactHours")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "description", source = "description")

    // =====================================================
    // AUDIT
    // =====================================================

    @Mapping(target = "createdByUserId", source = "createdByUserId")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdAt", source = "createdAt")

    @Mapping(target = "updatedByUserId", source = "updatedByUserId")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "updatedAt", source = "updatedAt")

    CurriculumResponseDto toResponse(
            Curriculum entity
    );


    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    // Never change CREATE audit fields from request
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)

    // UPDATE audit fields are handled by Service
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntity(
            CurriculumRequestDto request,
            @MappingTarget Curriculum entity
    );
}
