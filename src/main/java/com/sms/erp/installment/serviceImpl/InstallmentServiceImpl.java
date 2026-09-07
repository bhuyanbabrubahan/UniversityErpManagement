package com.sms.erp.installment.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.installment.dto.*;
import com.sms.erp.installment.entity.Installment;
import com.sms.erp.installment.repository.InstallmentRepository;
import com.sms.erp.installment.service.InstallmentService;
import com.sms.erp.installment.mapper.InstallmentMapper;
import com.sms.erp.installment.specification.InstallmentSpecification;

@Service
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {
    private final InstallmentRepository repository;
    private final InstallmentMapper mapper;
    @Override public Page<InstallmentResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<InstallmentResponseDto> search(InstallmentSearchDto search,int page,int size) { return repository.findAll(InstallmentSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public InstallmentResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Installment not found: " + id))); }
    @Override public InstallmentResponseDto create(InstallmentRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public InstallmentResponseDto update(Long id,InstallmentRequestDto request) { Installment current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Installment not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Installment not found: " + id); repository.deleteById(id); }
}
