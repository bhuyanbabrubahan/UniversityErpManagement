package com.location.village.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.village.dto.VillageRequestDto;
import com.location.village.dto.VillageResponseDto;
import com.location.village.entity.Village;

@Mapper(componentModel = "spring")
public interface VillageMapper {

	// Request -> Entity

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tehsil", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Audit
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	Village toEntity(VillageRequestDto dto);

	// Entity -> Response

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "villageName", source = "villageName")
	@Mapping(target = "pincode", source = "pincode")
	VillageResponseDto toDto(Village village);

	// Update Entity

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tehsil", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Audit
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	void updateEntity(@MappingTarget Village village, VillageRequestDto dto);

}