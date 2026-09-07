package com.sms.erp.research.mapper;

import org.mapstruct.*;
import com.sms.erp.research.entity.Research;
import com.sms.erp.research.dto.ResearchRequestDto;
import com.sms.erp.research.dto.ResearchResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResearchMapper {
    Research toEntity(ResearchRequestDto request);
    ResearchResponseDto toResponse(Research entity);
    void updateEntity(ResearchRequestDto request, @MappingTarget Research entity);
}
