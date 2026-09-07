package com.sms.erp.research.service;

import com.sms.erp.research.dto.*;
import org.springframework.data.domain.Page;

public interface ResearchService {
    Page<ResearchResponseDto> findAll(int page, int size);
    Page<ResearchResponseDto> search(ResearchSearchDto search, int page, int size);
    ResearchResponseDto findById(Long id);
    ResearchResponseDto create(ResearchRequestDto request);
    ResearchResponseDto update(Long id, ResearchRequestDto request);
    void delete(Long id);
}
