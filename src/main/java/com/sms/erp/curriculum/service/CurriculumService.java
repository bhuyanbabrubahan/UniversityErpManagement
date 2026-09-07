package com.sms.erp.curriculum.service;

import com.sms.erp.curriculum.dto.*;
import org.springframework.data.domain.Page;

public interface CurriculumService {
    Page<CurriculumResponseDto> findAll(int page, int size);
    Page<CurriculumResponseDto> search(CurriculumSearchDto search, int page, int size);
    CurriculumResponseDto findById(Long id);
    CurriculumResponseDto create(CurriculumRequestDto request);
    CurriculumResponseDto update(Long id, CurriculumRequestDto request);
    void delete(Long id);
}
