package com.sms.erp.hostel.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.hostel.dto.*;
import com.sms.erp.hostel.entity.Hostel;
import com.sms.erp.hostel.repository.HostelRepository;
import com.sms.erp.hostel.service.HostelService;
import com.sms.erp.hostel.mapper.HostelMapper;
import com.sms.erp.hostel.specification.HostelSpecification;

@Service
@RequiredArgsConstructor
public class HostelServiceImpl implements HostelService {
    private final HostelRepository repository;
    private final HostelMapper mapper;
    @Override public Page<HostelResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<HostelResponseDto> search(HostelSearchDto search,int page,int size) { return repository.findAll(HostelSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public HostelResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Hostel not found: " + id))); }
    @Override public HostelResponseDto create(HostelRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public HostelResponseDto update(Long id,HostelRequestDto request) { Hostel current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Hostel not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Hostel not found: " + id); repository.deleteById(id); }
}
