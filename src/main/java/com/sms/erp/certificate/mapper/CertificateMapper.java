package com.sms.erp.certificate.mapper;

import org.mapstruct.*;
import com.sms.erp.certificate.entity.Certificate;
import com.sms.erp.certificate.dto.CertificateRequestDto;
import com.sms.erp.certificate.dto.CertificateResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CertificateMapper {
    Certificate toEntity(CertificateRequestDto request);
    CertificateResponseDto toResponse(Certificate entity);
    void updateEntity(CertificateRequestDto request, @MappingTarget Certificate entity);
}
