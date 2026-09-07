package com.sms.security.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.security.dto.AdminRequestDto;
import com.sms.security.dto.AdminResponseDto;
import com.sms.security.entity.Admin;

@Mapper(componentModel = "spring")
public interface AdminMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "employeeCode", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
	@Mapping(target = "signatureImage", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedByUserId", ignore = true)
	Admin toEntity(AdminRequestDto dto);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "email", source = "user.email")
	@Mapping(target = "mobileNumber", source = "user.mobileNumber")
	@Mapping(target = "role", source = "user.role")
	@Mapping(target = "userStatus", source = "user.status")
	@Mapping(target = "fullName", expression = "java(admin.getFullName())")
	@Mapping(target = "villageId", source = "address.village.id")
	@Mapping(target = "village", source = "address.village.villageName")
	@Mapping(target = "pincode", source = "address.village.pincode")
	@Mapping(target = "tehsilId", source = "address.village.tehsil.id")
	@Mapping(target = "tehsil", source = "address.village.tehsil.tehsilName")
	@Mapping(target = "districtId", source = "address.village.tehsil.district.id")
	@Mapping(target = "district", source = "address.village.tehsil.district.districtName")
	@Mapping(target = "stateId", source = "address.village.tehsil.district.state.id")
	@Mapping(target = "state", source = "address.village.tehsil.district.state.stateName")
	@Mapping(target = "countryId", source = "address.village.tehsil.district.state.country.id")
	@Mapping(target = "country", source = "address.village.tehsil.district.state.country.countryName")
	AdminResponseDto toDto(Admin admin);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "employeeCode", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
	@Mapping(target = "signatureImage", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedByUserId", ignore = true)
	void updateEntity(AdminRequestDto dto, @MappingTarget Admin admin);
}
