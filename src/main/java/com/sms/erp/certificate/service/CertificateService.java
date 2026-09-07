package com.sms.erp.certificate.service;

import com.sms.erp.certificate.dto.*;
import org.springframework.data.domain.Page;

public interface CertificateService {
    Page<CertificateResponseDto> findAll(int page, int size);
    Page<CertificateResponseDto> search(CertificateSearchDto search, int page, int size);
    CertificateResponseDto findById(Long id);
    CertificateResponseDto create(CertificateRequestDto request);
    CertificateResponseDto update(Long id, CertificateRequestDto request);
    void delete(Long id);
}
