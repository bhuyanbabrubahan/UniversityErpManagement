package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.address.mapper.AddressMapper;
import com.sms.dto.StudentRequestDto;
import com.sms.dto.StudentResponseDto;
import com.sms.entity.Student;
import com.sms.security.dto.UserRequestDto;
import com.sms.security.mapper.UserMapper;

@Mapper(
    componentModel = "spring",
    uses = {
        UserMapper.class,
        AddressMapper.class,
        DepartmentMapper.class,
        CourseMapper.class,
        SemesterMapper.class
    }
)
public interface StudentMapper {

    // ==========================================================
    // StudentRequestDto -> Student Entity
    // ==========================================================

    @Mapping(target = "id", ignore = true)

    // ------------------------------------------
    // Relationships handled in Service
    // ------------------------------------------

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "currentSemester", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)

    // ------------------------------------------
    // Generated fields
    // ------------------------------------------

    @Mapping(target = "rollNumber", ignore = true)

    // ------------------------------------------
    // Audit fields
    // ------------------------------------------

    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    // ------------------------------------------
    // Record status handled by Service
    // ------------------------------------------

    @Mapping(target = "status", ignore = true)

    Student toEntity(StudentRequestDto request);


    // ==========================================================
    // User Registration -> Student Entity
    // ==========================================================

    /*
     * Is method ko agar kisi existing registration flow mein
     * use kar rahe ho to rakho.
     *
     * UserRequestDto ke jo fields Student ke saath match karte hain
     * MapStruct automatically map karega.
     */

    @Mapping(target = "id", ignore = true)

    // Relationships handled separately
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "currentSemester", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)

    // Generated
    @Mapping(target = "rollNumber", ignore = true)

    // System fields
    @Mapping(target = "status", ignore = true)

    // Audit
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    Student convertToEntity(UserRequestDto request);


    // ==========================================================
    // Student Entity -> StudentResponseDto
    // ==========================================================

    @Mapping(
        target = "fullName",
        expression = "java(student.getFullName())"
    )

    /*
     * Entity -> DTO nested objects
     *
     * Department
     * Course
     * Semester
     * Address
     * User
     *
     * inke liye mapper `uses` mein already defined hain.
     */

    StudentResponseDto convertToResponseDto(Student student);


    // ==========================================================
    // Update Existing Student
    // ==========================================================

    @BeanMapping(
        nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )

    @Mapping(target = "id", ignore = true)

    // ------------------------------------------
    // Relationships handled in Service
    // ------------------------------------------

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "currentSemester", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "user", ignore = true)

    // ------------------------------------------
    // Generated fields
    // ------------------------------------------

    @Mapping(target = "rollNumber", ignore = true)

    // ------------------------------------------
    // Audit fields
    // ------------------------------------------

    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)

    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    // ------------------------------------------
    // Record status
    // ------------------------------------------

    @Mapping(target = "status", ignore = true)

    void updateEntity(
        @MappingTarget Student student,
        StudentRequestDto dto
    );
}