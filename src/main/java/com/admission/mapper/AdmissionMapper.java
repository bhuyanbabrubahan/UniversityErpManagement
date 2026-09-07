package com.admission.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.admission.dto.AdmissionRequestDto;
import com.admission.dto.AdmissionResponseDto;
import com.admission.entity.Admission;

@Mapper(componentModel = "spring")
public interface AdmissionMapper {


    // ==========================================================
    // Request DTO -> Entity
    // ==========================================================

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "student", ignore = true)

    @Mapping(target = "department", ignore = true)

    @Mapping(target = "course", ignore = true)

    @Mapping(target = "semester", ignore = true)


    @Mapping(target = "admissionNumber", ignore = true)

    @Mapping(target = "admissionStatus", ignore = true)

    @Mapping(target = "status", ignore = true)


    Admission toEntity(
            AdmissionRequestDto dto
    );



    // ==========================================================
    // Entity -> Response DTO
    // ==========================================================


    @Mapping(
            target = "studentId",
            source = "student.id"
    )

    @Mapping(
            target = "studentRollNumber",
            source = "student.rollNumber"
    )

    @Mapping(
            target = "studentName",
            expression = """
            java(
                admission.getStudent() != null
                ? admission.getStudent().getFullName()
                : null
            )
            """
    )


    @Mapping(
            target = "departmentId",
            source = "department.id"
    )

    @Mapping(
            target = "departmentName",
            source = "department.departmentName"
    )


    @Mapping(
            target = "courseId",
            source = "course.id"
    )

    @Mapping(
            target = "courseName",
            source = "course.courseName"
    )


    @Mapping(
            target = "semesterId",
            source = "semester.id"
    )

    @Mapping(
            target = "semesterName",
            source = "semester.semesterName"
    )


    AdmissionResponseDto toDto(
            Admission admission
    );




    // ==========================================================
    // Update Entity
    // ==========================================================


    @BeanMapping(
            nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )


    @Mapping(target = "id", ignore = true)

    @Mapping(target = "student", ignore = true)

    @Mapping(target = "department", ignore = true)

    @Mapping(target = "course", ignore = true)

    @Mapping(target = "semester", ignore = true)


    @Mapping(target = "admissionNumber", ignore = true)

    @Mapping(target = "admissionStatus", ignore = true)

    @Mapping(target = "status", ignore = true)

    @Mapping(target = "createdAt", ignore = true)

    @Mapping(target = "updatedAt", ignore = true)


    // Creation Audit

    @Mapping(target = "createdByUserId", ignore = true)

    @Mapping(target = "createdBy", ignore = true)



    // Update Audit

    @Mapping(target = "updatedByUserId", ignore = true)

    @Mapping(target = "updatedBy", ignore = true)




    void updateEntity(
            @MappingTarget Admission admission,
            AdmissionRequestDto dto
    );

}