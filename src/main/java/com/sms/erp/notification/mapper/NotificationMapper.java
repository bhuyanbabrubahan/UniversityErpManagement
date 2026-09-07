package com.sms.erp.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sms.erp.notification.dto.NotificationRequestDto;
import com.sms.erp.notification.dto.NotificationResponseDto;
import com.sms.erp.notification.entity.Notification;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    Notification toEntity(NotificationRequestDto request);

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "createdByUserId", target = "createdByUserId")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "updatedByUserId", target = "updatedByUserId")
    @Mapping(source = "updatedBy", target = "updatedBy")
    NotificationResponseDto toResponse(Notification entity);

    void updateEntity(
        NotificationRequestDto request,
        @MappingTarget Notification entity
    );
}