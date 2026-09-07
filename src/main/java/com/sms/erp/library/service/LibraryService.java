package com.sms.erp.library.service;

import com.sms.erp.library.dto.*;
import org.springframework.data.domain.Page;

public interface LibraryService {
    Page<LibraryResponseDto> findAll(int page, int size);
    Page<LibraryResponseDto> search(LibrarySearchDto search, int page, int size);
    LibraryResponseDto findById(Long id);
    LibraryResponseDto create(LibraryRequestDto request);
    LibraryResponseDto update(Long id, LibraryRequestDto request);
    void delete(Long id);
}
