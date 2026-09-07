package com.sms.audit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sms.audit.dto.AuditLogResponseDto;
import com.sms.audit.entity.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    // =====================================
    // ENTITY -> RESPONSE DTO
    // =====================================

    @Mapping(target = "id", source = "id")
    @Mapping(target = "action", source = "action")
    @Mapping(target = "moduleName", source = "moduleName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "performedBy", source = "performedBy")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "ipAddress", source = "ipAddress")
    @Mapping(target = "requestMethod", source = "requestMethod")
    @Mapping(target = "requestUrl", source = "requestUrl")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "status", source = "status")
    AuditLogResponseDto convertToDto(
            AuditLog auditLog
    );

}