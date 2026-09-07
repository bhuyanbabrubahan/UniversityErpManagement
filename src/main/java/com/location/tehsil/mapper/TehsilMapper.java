package com.location.tehsil.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.tehsil.dto.TehsilRequestDto;
import com.location.tehsil.dto.TehsilResponseDto;
import com.location.tehsil.entity.Tehsil;

@Mapper(componentModel = "spring")
public interface TehsilMapper {

	// Request -> Entity

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "district", ignore = true)
	@Mapping(target = "villages", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Audit
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	Tehsil toEntity(TehsilRequestDto dto);

	// Entity -> Response

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "id")
	@Mapping(target = "tehsilName", source = "tehsilName")

	TehsilResponseDto toDto(Tehsil tehsil);

	// Update Entity

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "district", ignore = true)
	@Mapping(target = "villages", ignore = true)
	@Mapping(target = "status", ignore = true)

	// Audit
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)

	void updateEntity(@MappingTarget Tehsil tehsil, TehsilRequestDto dto);

}