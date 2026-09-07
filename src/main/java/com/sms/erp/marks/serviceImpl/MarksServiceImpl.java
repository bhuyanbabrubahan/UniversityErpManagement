package com.sms.erp.marks.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.marks.dto.*;
import com.sms.erp.marks.entity.Marks;
import com.sms.erp.marks.repository.MarksRepository;
import com.sms.erp.marks.service.MarksService;
import com.sms.erp.marks.mapper.MarksMapper;
import com.sms.erp.marks.specification.MarksSpecification;

@Service
@RequiredArgsConstructor
public class MarksServiceImpl implements MarksService {
    private final MarksRepository repository;
    private final MarksMapper mapper;
    @Override public Page<MarksResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<MarksResponseDto> search(MarksSearchDto search,int page,int size) { return repository.findAll(MarksSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public MarksResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Marks not found: " + id))); }
    @Override public MarksResponseDto create(MarksRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public MarksResponseDto update(Long id,MarksRequestDto request) { Marks current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Marks not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Marks not found: " + id); repository.deleteById(id); }
}
