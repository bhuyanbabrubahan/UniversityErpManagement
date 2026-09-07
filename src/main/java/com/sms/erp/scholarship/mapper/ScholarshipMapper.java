package com.sms.erp.scholarship.mapper;

import org.mapstruct.*;
import com.sms.erp.scholarship.entity.Scholarship;
import com.sms.erp.scholarship.dto.ScholarshipRequestDto;
import com.sms.erp.scholarship.dto.ScholarshipResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScholarshipMapper {
    Scholarship toEntity(ScholarshipRequestDto request);
    ScholarshipResponseDto toResponse(Scholarship entity);
    void updateEntity(ScholarshipRequestDto request, @MappingTarget Scholarship entity);
}
