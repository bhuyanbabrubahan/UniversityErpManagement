package com.location.country.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.country.dto.CountryRequestDto;
import com.location.country.dto.CountryResponseDto;
import com.location.country.entity.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    // =====================================================
    // REQUEST -> ENTITY
    // =====================================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "states", ignore = true)

    // Audit fields
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)

    Country toEntity(CountryRequestDto dto);

    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "countryName", source = "countryName")
    CountryResponseDto toDto(Country country);

    // =====================================================
    // UPDATE ENTITY
    // =====================================================

    @BeanMapping(
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "states", ignore = true)

    // Audit fields
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)

    void updateEntity(
            @MappingTarget Country country,
            CountryRequestDto dto
    );
}