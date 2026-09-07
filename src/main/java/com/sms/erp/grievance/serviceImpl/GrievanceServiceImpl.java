package com.sms.erp.grievance.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.grievance.dto.*;
import com.sms.erp.grievance.entity.Grievance;
import com.sms.erp.grievance.repository.GrievanceRepository;
import com.sms.erp.grievance.service.GrievanceService;
import com.sms.erp.grievance.mapper.GrievanceMapper;
import com.sms.erp.grievance.specification.GrievanceSpecification;

@Service
@RequiredArgsConstructor
public class GrievanceServiceImpl implements GrievanceService {
    private final GrievanceRepository repository;
    private final GrievanceMapper mapper;
    @Override public Page<GrievanceResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<GrievanceResponseDto> search(GrievanceSearchDto search,int page,int size) { return repository.findAll(GrievanceSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public GrievanceResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Grievance not found: " + id))); }
    @Override public GrievanceResponseDto create(GrievanceRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public GrievanceResponseDto update(Long id,GrievanceRequestDto request) { Grievance current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Grievance not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Grievance not found: " + id); repository.deleteById(id); }
}
