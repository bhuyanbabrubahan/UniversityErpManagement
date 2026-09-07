package com.sms.erp.hod.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sms.erp.hod.dto.HodRequestDto;
import com.sms.erp.hod.dto.HodResponseDto;
import com.sms.erp.hod.entity.Hod;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HodMapper {

    // =====================================================
    // REQUEST -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    Hod toEntity(
            HodRequestDto request
    );


    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    @Mapping(target = "id", source = "id")

    @Mapping(target = "code", source = "code")
    @Mapping(target = "employeeCode", source = "employeeCode")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "designation", source = "designation")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "description", source = "description")

    // =====================================================
    // AUDIT
    // =====================================================

    @Mapping(
            target = "createdByUserId",
            source = "createdByUserId"
    )

    @Mapping(
            target = "createdBy",
            source = "createdBy"
    )

    @Mapping(
            target = "createdAt",
            source = "createdAt"
    )

    @Mapping(
            target = "updatedByUserId",
            source = "updatedByUserId"
    )

    @Mapping(
            target = "updatedBy",
            source = "updatedBy"
    )

    @Mapping(
            target = "updatedAt",
            source = "updatedAt"
    )

    HodResponseDto toResponse(
            Hod entity
    );


    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    // Never change CREATE audit fields from request
    @Mapping(
            target = "createdByUserId",
            ignore = true
    )

    @Mapping(
            target = "createdBy",
            ignore = true
    )

    @Mapping(
            target = "createdAt",
            ignore = true
    )

    // UPDATE audit fields are handled by Service
    @Mapping(
            target = "updatedByUserId",
            ignore = true
    )

    @Mapping(
            target = "updatedBy",
            ignore = true
    )

    @Mapping(
            target = "updatedAt",
            ignore = true
    )

    void updateEntity(
            HodRequestDto request,
            @MappingTarget Hod entity
    );
}
