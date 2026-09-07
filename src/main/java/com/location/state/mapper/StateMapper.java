package com.location.state.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.location.state.dto.StateRequestDto;
import com.location.state.dto.StateResponseDto;
import com.location.state.entity.State;

@Mapper(componentModel = "spring")
public interface StateMapper {

    // Request -> Entity

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "districts", ignore = true)
    @Mapping(target = "status", ignore = true)

    // Audit
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    State toEntity(StateRequestDto dto);

    // Entity -> Response

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "stateName", source = "stateName")

    StateResponseDto toDto(State state);

    // Update Entity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "districts", ignore = true)
    @Mapping(target = "status", ignore = true)

    // Audit
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntity(@MappingTarget State state, StateRequestDto dto);

}