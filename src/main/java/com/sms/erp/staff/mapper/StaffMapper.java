package com.sms.erp.staff.mapper;

import org.mapstruct.*;
import com.sms.erp.staff.entity.Staff;
import com.sms.erp.staff.dto.StaffRequestDto;
import com.sms.erp.staff.dto.StaffResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
    Staff toEntity(StaffRequestDto request);
    StaffResponseDto toResponse(Staff entity);
    void updateEntity(StaffRequestDto request, @MappingTarget Staff entity);
}
