package com.sms.erp.installment.mapper;

import org.mapstruct.*;
import com.sms.erp.installment.entity.Installment;
import com.sms.erp.installment.dto.InstallmentRequestDto;
import com.sms.erp.installment.dto.InstallmentResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstallmentMapper {
    Installment toEntity(InstallmentRequestDto request);
    InstallmentResponseDto toResponse(Installment entity);
    void updateEntity(InstallmentRequestDto request, @MappingTarget Installment entity);
}
