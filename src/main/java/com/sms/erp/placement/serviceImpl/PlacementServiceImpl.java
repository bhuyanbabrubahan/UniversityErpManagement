package com.sms.erp.placement.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.placement.dto.*;
import com.sms.erp.placement.entity.Placement;
import com.sms.erp.placement.repository.PlacementRepository;
import com.sms.erp.placement.service.PlacementService;
import com.sms.erp.placement.mapper.PlacementMapper;
import com.sms.erp.placement.specification.PlacementSpecification;

@Service
@RequiredArgsConstructor
public class PlacementServiceImpl implements PlacementService {
    private final PlacementRepository repository;
    private final PlacementMapper mapper;
    @Override public Page<PlacementResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<PlacementResponseDto> search(PlacementSearchDto search,int page,int size) { return repository.findAll(PlacementSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public PlacementResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Placement not found: " + id))); }
    @Override public PlacementResponseDto create(PlacementRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public PlacementResponseDto update(Long id,PlacementRequestDto request) { Placement current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Placement not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Placement not found: " + id); repository.deleteById(id); }
}
