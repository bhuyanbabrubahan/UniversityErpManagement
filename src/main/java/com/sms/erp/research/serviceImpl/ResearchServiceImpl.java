package com.sms.erp.research.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.research.dto.*;
import com.sms.erp.research.entity.Research;
import com.sms.erp.research.repository.ResearchRepository;
import com.sms.erp.research.service.ResearchService;
import com.sms.erp.research.mapper.ResearchMapper;
import com.sms.erp.research.specification.ResearchSpecification;

@Service
@RequiredArgsConstructor
public class ResearchServiceImpl implements ResearchService {
    private final ResearchRepository repository;
    private final ResearchMapper mapper;
    @Override public Page<ResearchResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<ResearchResponseDto> search(ResearchSearchDto search,int page,int size) { return repository.findAll(ResearchSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public ResearchResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Research not found: " + id))); }
    @Override public ResearchResponseDto create(ResearchRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public ResearchResponseDto update(Long id,ResearchRequestDto request) { Research current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Research not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Research not found: " + id); repository.deleteById(id); }
}
