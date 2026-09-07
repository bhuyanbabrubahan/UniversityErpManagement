package com.sms.erp.scholarship.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.scholarship.dto.*;
import com.sms.erp.scholarship.entity.Scholarship;
import com.sms.erp.scholarship.repository.ScholarshipRepository;
import com.sms.erp.scholarship.service.ScholarshipService;
import com.sms.erp.scholarship.mapper.ScholarshipMapper;
import com.sms.erp.scholarship.specification.ScholarshipSpecification;

@Service
@RequiredArgsConstructor
public class ScholarshipServiceImpl implements ScholarshipService {
    private final ScholarshipRepository repository;
    private final ScholarshipMapper mapper;
    @Override public Page<ScholarshipResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<ScholarshipResponseDto> search(ScholarshipSearchDto search,int page,int size) { return repository.findAll(ScholarshipSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public ScholarshipResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + id))); }
    @Override public ScholarshipResponseDto create(ScholarshipRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public ScholarshipResponseDto update(Long id,ScholarshipRequestDto request) { Scholarship current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Scholarship not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Scholarship not found: " + id); repository.deleteById(id); }
}
