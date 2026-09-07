package com.sms.erp.timetable.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sms.erp.timetable.dto.TimetableRequestDto;
import com.sms.erp.timetable.dto.TimetableResponseDto;
import com.sms.erp.timetable.entity.Timetable;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TimetableMapper {

    // =====================================================
    // REQUEST -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

    
    Timetable toEntity(
            TimetableRequestDto request
    );

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    @Mapping(
            target = "id",
            source = "id"
    )

    @Mapping(
            target = "code",
            source = "code"
    )
    @Mapping(
            target = "academicYear",
            source = "academicYear"
    )
    @Mapping(
            target = "semester",
            source = "semester"
    )
    @Mapping(
            target = "course",
            source = "course"
    )
    @Mapping(
            target = "section",
            source = "section"
    )
    @Mapping(
            target = "subject",
            source = "subject"
    )
    @Mapping(
            target = "faculty",
            source = "faculty"
    )
    @Mapping(
            target = "room",
            source = "room"
    )
    @Mapping(
            target = "dayOfWeek",
            source = "dayOfWeek"
    )
    @Mapping(
            target = "startTime",
            source = "startTime"
    )
    @Mapping(
            target = "endTime",
            source = "endTime"
    )
    @Mapping(
            target = "period",
            source = "period"
    )
    @Mapping(
            target = "status",
            source = "status"
    )
    @Mapping(
            target = "description",
            source = "description"
    )

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

    TimetableResponseDto toResponse(
            Timetable entity
    );

    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)

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
            TimetableRequestDto request,
            @MappingTarget Timetable entity
    );
}