package com.sms.erp.marks.service;

import com.sms.erp.marks.dto.*;
import org.springframework.data.domain.Page;

public interface MarksService {
    Page<MarksResponseDto> findAll(int page, int size);
    Page<MarksResponseDto> search(MarksSearchDto search, int page, int size);
    MarksResponseDto findById(Long id);
    MarksResponseDto create(MarksRequestDto request);
    MarksResponseDto update(Long id, MarksRequestDto request);
    void delete(Long id);
}
