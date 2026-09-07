package com.location.district.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.district.dto.DistrictRequestDto;
import com.location.district.dto.DistrictResponseDto;
import com.location.district.entity.District;


@Mapper(componentModel = "spring")
public interface DistrictMapper {

    // Request -> Entity

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "tehsils", ignore = true)
    @Mapping(target = "status", ignore = true)

    // Audit
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    District toEntity(DistrictRequestDto dto);

    // Entity -> Response

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "districtName", source = "districtName")
	DistrictResponseDto toDto(District district);

	// Update Entity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)

    @Mapping(target = "tehsils", ignore = true)
    @Mapping(target = "status", ignore = true)

    // Audit
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntity(@MappingTarget District district, DistrictRequestDto dto);

}