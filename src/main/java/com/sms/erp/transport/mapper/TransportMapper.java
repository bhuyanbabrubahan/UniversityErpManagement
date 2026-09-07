package com.sms.erp.transport.mapper;

import org.mapstruct.*;
import com.sms.erp.transport.entity.Transport;
import com.sms.erp.transport.dto.TransportRequestDto;
import com.sms.erp.transport.dto.TransportResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransportMapper {
    Transport toEntity(TransportRequestDto request);
    TransportResponseDto toResponse(Transport entity);
    void updateEntity(TransportRequestDto request, @MappingTarget Transport entity);
}
