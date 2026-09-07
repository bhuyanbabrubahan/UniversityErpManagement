package com.sms.erp.staff.service;

import com.sms.erp.staff.dto.*;
import org.springframework.data.domain.Page;

public interface StaffService {
    Page<StaffResponseDto> findAll(int page, int size);
    Page<StaffResponseDto> search(StaffSearchDto search, int page, int size);
    StaffResponseDto findById(Long id);
    StaffResponseDto create(StaffRequestDto request);
    StaffResponseDto update(Long id, StaffRequestDto request);
    void delete(Long id);
}
