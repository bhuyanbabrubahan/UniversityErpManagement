package com.sms.erp.certificate.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.certificate.dto.*;
import com.sms.erp.certificate.entity.Certificate;
import com.sms.erp.certificate.repository.CertificateRepository;
import com.sms.erp.certificate.service.CertificateService;
import com.sms.erp.certificate.mapper.CertificateMapper;
import com.sms.erp.certificate.specification.CertificateSpecification;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository repository;
    private final CertificateMapper mapper;
    @Override public Page<CertificateResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<CertificateResponseDto> search(CertificateSearchDto search,int page,int size) { return repository.findAll(CertificateSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public CertificateResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Certificate not found: " + id))); }
    @Override public CertificateResponseDto create(CertificateRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public CertificateResponseDto update(Long id,CertificateRequestDto request) { Certificate current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Certificate not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Certificate not found: " + id); repository.deleteById(id); }
}
