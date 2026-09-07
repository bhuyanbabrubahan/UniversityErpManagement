package com.sms.erp.transport.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.transport.dto.*;
import com.sms.erp.transport.entity.Transport;
import com.sms.erp.transport.repository.TransportRepository;
import com.sms.erp.transport.service.TransportService;
import com.sms.erp.transport.mapper.TransportMapper;
import com.sms.erp.transport.specification.TransportSpecification;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final TransportRepository repository;
    private final TransportMapper mapper;
    @Override public Page<TransportResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<TransportResponseDto> search(TransportSearchDto search,int page,int size) { return repository.findAll(TransportSpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public TransportResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transport not found: " + id))); }
    @Override public TransportResponseDto create(TransportRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public TransportResponseDto update(Long id,TransportRequestDto request) { Transport current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transport not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Transport not found: " + id); repository.deleteById(id); }
}
