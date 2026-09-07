package com.sms.erp.scholarship.service;

import com.sms.erp.scholarship.dto.*;
import org.springframework.data.domain.Page;

public interface ScholarshipService {
    Page<ScholarshipResponseDto> findAll(int page, int size);
    Page<ScholarshipResponseDto> search(ScholarshipSearchDto search, int page, int size);
    ScholarshipResponseDto findById(Long id);
    ScholarshipResponseDto create(ScholarshipRequestDto request);
    ScholarshipResponseDto update(Long id, ScholarshipRequestDto request);
    void delete(Long id);
}
