package com.sms.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.dto.FacultyRequestDto;
import com.sms.dto.FacultyResponseDto;
import com.sms.entity.Faculty;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    // =========================================================
    // REQUEST -> ENTITY
    // =========================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "subjectMappings", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "signatureImage", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    Faculty convertToEntity(FacultyRequestDto dto);


    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    @Mapping(target = "userId", source = "user.id")

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "mobileNumber", source = "user.mobileNumber")

    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "userStatus", source = "user.status")

    @Mapping(
        target = "fullName",
        expression = "java(faculty.getFullName())"
    )

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.departmentName")

    // =========================================================
    // ADDRESS
    // =========================================================

    @Mapping(target = "addressId", source = "address.id")

    @Mapping(target = "houseNumber", source = "address.houseNumber")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "landmark", source = "address.landmark")
    @Mapping(target = "addressType", source = "address.addressType")

    // =========================================================
    // LOCATION
    // =========================================================

    @Mapping(target = "villageId", source = "address.village.id")
    @Mapping(target = "villageName", source = "address.village.villageName")

    @Mapping(target = "pincode", source = "address.village.pincode")

    @Mapping(
        target = "tehsilId",
        source = "address.village.tehsil.id"
    )

    @Mapping(
        target = "tehsilName",
        source = "address.village.tehsil.tehsilName"
    )

    @Mapping(
        target = "districtId",
        source = "address.village.tehsil.district.id"
    )

    @Mapping(
        target = "districtName",
        source = "address.village.tehsil.district.districtName"
    )

    @Mapping(
        target = "stateId",
        source = "address.village.tehsil.district.state.id"
    )

    @Mapping(
        target = "stateName",
        source = "address.village.tehsil.district.state.stateName"
    )

    @Mapping(
        target = "countryId",
        source = "address.village.tehsil.district.state.country.id"
    )

    @Mapping(
        target = "countryName",
        source = "address.village.tehsil.district.state.country.countryName"
    )

    // =========================================================
    // AUDIT
    // =========================================================

    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "updatedDate", source = "updatedAt")

    FacultyResponseDto convertToResponseDto(Faculty faculty);


    // =========================================================
    // UPDATE ENTITY
    // =========================================================

    @BeanMapping(
        nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE
    )

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeCode", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "subjectMappings", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "signatureImage", ignore = true)

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)

    void updateEntity(
        @MappingTarget Faculty faculty,
        FacultyRequestDto dto
    );
}