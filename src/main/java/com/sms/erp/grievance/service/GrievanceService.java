package com.sms.erp.grievance.service;

import com.sms.erp.grievance.dto.*;
import org.springframework.data.domain.Page;

public interface GrievanceService {
    Page<GrievanceResponseDto> findAll(int page, int size);
    Page<GrievanceResponseDto> search(GrievanceSearchDto search, int page, int size);
    GrievanceResponseDto findById(Long id);
    GrievanceResponseDto create(GrievanceRequestDto request);
    GrievanceResponseDto update(Long id, GrievanceRequestDto request);
    void delete(Long id);
}
