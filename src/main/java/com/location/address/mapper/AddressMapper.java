package com.location.address.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.location.address.dto.AddressRequestDto;
import com.location.address.dto.AddressResponseDto;
import com.location.address.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "villageId", source = "village.id")
    @Mapping(target = "village", source = "village.villageName")
    @Mapping(target = "tehsilId", source = "village.tehsil.id")
    @Mapping(target = "tehsil", source = "village.tehsil.tehsilName")
    @Mapping(target = "districtId", source = "village.tehsil.district.id")
    @Mapping(target = "district", source = "village.tehsil.district.districtName")
    @Mapping(target = "stateId", source = "village.tehsil.district.state.id")
    @Mapping(target = "state", source = "village.tehsil.district.state.stateName")
    @Mapping(target = "countryId", source = "village.tehsil.district.state.country.id")
    @Mapping(target = "country", source = "village.tehsil.district.state.country.countryName")
    @Mapping(target = "pincode", source = "village.pincode")
    AddressResponseDto toDto(Address address);

    // REQUEST -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "village", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    Address toEntity(AddressRequestDto dto);

    // REQUEST -> EXISTING ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "village", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUserId", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    void updateEntity(@MappingTarget Address address, AddressRequestDto dto);
}
