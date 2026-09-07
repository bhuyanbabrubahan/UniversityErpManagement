package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.DepartmentRequestDto;
import com.sms.dto.DepartmentResponseDto;
import com.sms.entity.Department;



@Mapper(componentModel = "spring")
public interface DepartmentMapper {


    // ==========================================================
    // DTO -> ENTITY (CREATE)
    // ==========================================================


    @Mapping(
            target = "id",
            ignore = true
    )


    @Mapping(
            target = "students",
            ignore = true
    )


    @Mapping(
            target = "faculties",
            ignore = true
    )


    // ==========================================================
    // Audit Fields
    // ==========================================================

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


    Department toEntity(
            DepartmentRequestDto dto
    );





    // ==========================================================
    // ENTITY -> RESPONSE DTO
    // ==========================================================


    DepartmentResponseDto toDto(
            Department department
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
            target = "students",
            ignore = true
    )


    @Mapping(
            target = "faculties",
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


    // ==========================================================
    // Audit Fields
    // ==========================================================

    @Mapping(
            target = "createdByUserId",
            ignore = true
    )

    @Mapping(
            target = "createdBy",
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

 
    void updateEntity(
            @MappingTarget Department department,
            DepartmentRequestDto dto
    );

}