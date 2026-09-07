package com.sms.erp.staff.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.staff.dto.*;
import com.sms.erp.staff.entity.Staff;
import com.sms.erp.staff.repository.StaffRepository;
import com.sms.erp.staff.service.StaffService;
import com.sms.erp.staff.mapper.StaffMapper;
import com.sms.erp.staff.specification.StaffSpecification;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository repository;
    private final StaffMapper mapper;
    @Override public Page<StaffResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<StaffResponseDto> search(StaffSearchDto search,int page,int size) { return repository.findAll(StaffSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public StaffResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Staff not found: " + id))); }
    @Override public StaffResponseDto create(StaffRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public StaffResponseDto update(Long id,StaffRequestDto request) { Staff current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Staff not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Staff not found: " + id); repository.deleteById(id); }
}
