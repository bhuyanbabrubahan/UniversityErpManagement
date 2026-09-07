package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.CourseRequestDto;
import com.sms.dto.CourseResponseDto;
import com.sms.entity.Course;


@Mapper(componentModel = "spring")
public interface CourseMapper {


    // ==========================================================
    // DTO -> ENTITY (CREATE)
    // ==========================================================


    @Mapping(
            target = "id",
            ignore = true
    )


    @Mapping(
            target = "department",
            ignore = true
    )

    @Mapping(
            target = "semesters",
            ignore = true
    )
    
    @Mapping(
            target = "status",
            ignore = true
    )


    // Audit Information

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


    Course toEntity(
            CourseRequestDto dto
    );




    // ==========================================================
    // ENTITY -> RESPONSE DTO
    // ==========================================================


    @Mapping(
            source = "department.id",
            target = "departmentId"
    )


    @Mapping(
            source = "department.departmentName",
            target = "departmentName"
    )


    CourseResponseDto toDto(
            Course course
    );





    // ==========================================================
    // UPDATE ENTITY
    // ==========================================================


    @BeanMapping(
            nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )


    @Mapping(
            target = "id",
            ignore = true
    )


    @Mapping(
            target = "department",
            ignore = true
    )

    @Mapping(
            target = "semesters",
            ignore = true
    )

  

    // Audit Information

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
            @MappingTarget Course course,
            CourseRequestDto dto
    );

}