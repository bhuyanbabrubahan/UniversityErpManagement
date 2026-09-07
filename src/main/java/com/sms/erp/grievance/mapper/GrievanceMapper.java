package com.sms.erp.grievance.mapper;

import org.mapstruct.*;
import com.sms.erp.grievance.entity.Grievance;
import com.sms.erp.grievance.dto.GrievanceRequestDto;
import com.sms.erp.grievance.dto.GrievanceResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GrievanceMapper {
    Grievance toEntity(GrievanceRequestDto request);
    GrievanceResponseDto toResponse(Grievance entity);
    void updateEntity(GrievanceRequestDto request, @MappingTarget Grievance entity);
}
