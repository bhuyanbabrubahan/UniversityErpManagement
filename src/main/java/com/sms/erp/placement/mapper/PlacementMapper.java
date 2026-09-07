package com.sms.erp.placement.mapper;

import org.mapstruct.*;
import com.sms.erp.placement.entity.Placement;
import com.sms.erp.placement.dto.PlacementRequestDto;
import com.sms.erp.placement.dto.PlacementResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlacementMapper {
    Placement toEntity(PlacementRequestDto request);
    PlacementResponseDto toResponse(Placement entity);
    void updateEntity(PlacementRequestDto request, @MappingTarget Placement entity);
}
