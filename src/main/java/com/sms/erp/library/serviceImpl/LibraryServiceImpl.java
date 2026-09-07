package com.sms.erp.library.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.sms.erp.library.dto.*;
import com.sms.erp.library.entity.Library;
import com.sms.erp.library.repository.LibraryRepository;
import com.sms.erp.library.service.LibraryService;
import com.sms.erp.library.mapper.LibraryMapper;
import com.sms.erp.library.specification.LibrarySpecification;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository repository;
    private final LibraryMapper mapper;
    @Override public Page<LibraryResponseDto> findAll(int page,int size) { return repository.findAll(PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public Page<LibraryResponseDto> search(LibrarySearchDto search,int page,int size) { return repository.findAll(LibrarySpecification.bySearch(search), PageRequest.of(page,size,Sort.by("id").descending())).map(mapper::toResponse); }
    @Override public LibraryResponseDto findById(Long id) { return mapper.toResponse(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Library not found: " + id))); }
    @Override public LibraryResponseDto create(LibraryRequestDto request) { return mapper.toResponse(repository.save(mapper.toEntity(request))); }
    @Override public LibraryResponseDto update(Long id,LibraryRequestDto request) { Library current=repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Library not found: " + id)); mapper.updateEntity(request,current); return mapper.toResponse(repository.save(current)); }
    @Override public void delete(Long id) { if (!repository.existsById(id)) throw new IllegalArgumentException("Library not found: " + id); repository.deleteById(id); }
}
