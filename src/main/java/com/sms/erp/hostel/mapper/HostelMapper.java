package com.sms.erp.hostel.mapper;

import org.mapstruct.*;
import com.sms.erp.hostel.entity.Hostel;
import com.sms.erp.hostel.dto.HostelRequestDto;
import com.sms.erp.hostel.dto.HostelResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HostelMapper {
    Hostel toEntity(HostelRequestDto request);
    HostelResponseDto toResponse(Hostel entity);
    void updateEntity(HostelRequestDto request, @MappingTarget Hostel entity);
}
