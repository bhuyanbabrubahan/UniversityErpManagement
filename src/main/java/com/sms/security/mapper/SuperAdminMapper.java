package com.sms.security.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sms.security.dto.SuperAdminRequestDto;
import com.sms.security.dto.SuperAdminResponseDto;
import com.sms.security.entity.SuperAdmin;

@Mapper(componentModel="spring")
public interface SuperAdminMapper {

	// ==========================================================
	// SuperAdminRequestDto -> SuperAdmin Entity
	// ==========================================================

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "employeeCode", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Existing Audit Fields
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	// Base Audit Fields
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedByUserId", ignore = true)
	
	SuperAdmin toEntity(SuperAdminRequestDto dto);


	@Mapping(target = "email", source = "user.email")
	@Mapping(target = "mobileNumber", source = "user.mobileNumber")
	@Mapping(target = "role", source = "user.role")

	@Mapping(
	    target = "fullName",
	    expression = "java(superAdmin.getFullName())"
	)

	@Mapping(target = "designation", source = "designation")
	@Mapping(target = "profileImage", source = "profileImage")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dateOfBirth", source = "dateOfBirth")
	@Mapping(target = "salary", source = "salary")
	@Mapping(target = "joiningDate", source = "joiningDate")
	@Mapping(target = "remarks", source = "remarks")
	@Mapping(target = "employeeCode", source = "employeeCode")
	@Mapping(target = "status", source = "status")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")

	// Address
	@Mapping(target = "houseNumber", source = "address.houseNumber")
	@Mapping(target = "street", source = "address.street")
	@Mapping(target = "landmark", source = "address.landmark")
	@Mapping(target = "addressType", source = "address.addressType")

	@Mapping(target = "village", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null
	        ? superAdmin.getAddress().getVillage().getVillageName()
	        : null
	    )
	    """)

	@Mapping(target = "pincode", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null
	        ? superAdmin.getAddress().getVillage().getPincode()
	        : null
	    )
	    """)

	@Mapping(target = "tehsil", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null &&
	        superAdmin.getAddress().getVillage().getTehsil() != null
	        ? superAdmin.getAddress().getVillage().getTehsil().getTehsilName()
	        : null
	    )
	    """)

	@Mapping(target = "district", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null &&
	        superAdmin.getAddress().getVillage().getTehsil() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict() != null
	        ? superAdmin.getAddress().getVillage().getTehsil().getDistrict().getDistrictName()
	        : null
	    )
	    """)

	@Mapping(target = "state", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null &&
	        superAdmin.getAddress().getVillage().getTehsil() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict().getState() != null
	        ? superAdmin.getAddress().getVillage().getTehsil().getDistrict().getState().getStateName()
	        : null
	    )
	    """)

	@Mapping(target = "country", expression = """
	    java(
	        superAdmin.getAddress() != null &&
	        superAdmin.getAddress().getVillage() != null &&
	        superAdmin.getAddress().getVillage().getTehsil() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict().getState() != null &&
	        superAdmin.getAddress().getVillage().getTehsil().getDistrict().getState().getCountry() != null
	        ? superAdmin.getAddress().getVillage().getTehsil().getDistrict().getState().getCountry().getCountryName()
	        : null
	    )
	    """)

	SuperAdminResponseDto toDto(SuperAdmin superAdmin);



 
	// ==========================================================
	// Update Existing Super Admin
	// ==========================================================

	@BeanMapping(
	        nullValuePropertyMappingStrategy =
	                NullValuePropertyMappingStrategy.IGNORE
	)

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "employeeCode", ignore = true)
	@Mapping(target = "profileImage", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Existing Audit Fields
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	// Base Audit Fields
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdByUserId", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedByUserId", ignore = true)

	void updateEntity(
	        SuperAdminRequestDto dto,
	        @MappingTarget SuperAdmin superAdmin
	);

}