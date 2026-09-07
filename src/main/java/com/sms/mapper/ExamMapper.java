package com.sms.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sms.dto.ExamRequestDto;
import com.sms.dto.ExamResponseDto;
import com.sms.entity.Exam;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExamMapper {


    // ==========================
    // DTO -> ENTITY
    // ==========================


    @Mapping(
            target = "id",
            ignore = true
    )

    @Mapping(
            target = "subject",
            ignore = true
    )

    @Mapping(
            target = "semester",
            ignore = true
    )

    @Mapping(
            target = "status",
            ignore = true
    )
    
    // User Audit Information
    @Mapping(
            target = "createdByUserId",
            ignore = true
    )

    @Mapping(
            target = "createdBy",
            ignore = true
    )

    @Mapping(
            target = "createdDate",
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
            target = "updatedDate",
            ignore = true
    )
    Exam toEntity(
            ExamRequestDto dto
    );



    // ==========================
    // ENTITY -> DTO
    // ==========================


    @Mapping(
            source = "subject.id",
            target = "subjectId"
    )

    @Mapping(
            source = "subject.subjectName",
            target = "subjectName"
    )


    @Mapping(
            source = "semester.id",
            target = "semesterId"
    )

    @Mapping(
            source = "semester.semesterName",
            target = "semesterName"
    )


    ExamResponseDto toDto(
            Exam exam
    );



    // ==========================
    // UPDATE
    // ==========================


    @BeanMapping(
            nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )


    @Mapping(
            target = "id",
            ignore = true
    )


    @Mapping(
            target = "subject",
            ignore = true
    )


    @Mapping(
            target = "semester",
            ignore = true
    )


    @Mapping(
            target = "status",
            ignore = true
    )

    // User Audit Information
    @Mapping(
            target = "createdByUserId",
            ignore = true
    )

    @Mapping(
            target = "createdBy",
            ignore = true
    )

    @Mapping(
            target = "createdDate",
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
            target = "updatedDate",
            ignore = true
    )

    void updateEntity(
            @MappingTarget Exam exam,
            ExamRequestDto dto
    );

}