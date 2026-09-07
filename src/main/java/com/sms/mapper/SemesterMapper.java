package com.sms.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.SemesterRequestDto;
import com.sms.dto.SemesterResponseDto;
import com.sms.entity.Semester;


@Mapper(componentModel = "spring")
public interface SemesterMapper {


    // ==================================================
    // DTO -> ENTITY
    // ==================================================

    @Mapping(
            target = "id",
            ignore = true
    )

    @Mapping(
            target = "course",
            ignore = true
    )

    @Mapping(
            target = "subjects",
            ignore = true
    )

    @Mapping(
            target = "status",
            ignore = true
    )

    @Mapping(
            target = "createdAt",
            ignore = true
    )

    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    Semester toEntity(
            SemesterRequestDto dto
    );




    // ==================================================
    // ENTITY -> DTO
    // ==================================================

    @Mapping(
            source = "course.id",
            target = "courseId"
    )

    @Mapping(
            source = "course.courseCode",
            target = "courseCode"
    )

    @Mapping(
            source = "course.courseName",
            target = "courseName"
    )

    SemesterResponseDto toDto(
            Semester semester
    );




    // ==================================================
    // UPDATE
    // ==================================================

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )

    @Mapping(
            target = "id",
            ignore = true
    )

    @Mapping(
            target = "course",
            ignore = true
    )

    @Mapping(
            target = "subjects",
            ignore = true
    )

    @Mapping(
            target = "status",
            ignore = true
    )

    @Mapping(
            target = "createdAt",
            ignore = true
    )

    @Mapping(
            target = "updatedAt",
            ignore = true
    )
    
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    void updateEntity(

            @MappingTarget
            Semester semester,

            SemesterRequestDto dto

    );

}