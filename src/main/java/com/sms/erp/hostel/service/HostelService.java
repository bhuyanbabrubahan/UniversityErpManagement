package com.sms.erp.hostel.service;

import com.sms.erp.hostel.dto.*;
import org.springframework.data.domain.Page;

public interface HostelService {
    Page<HostelResponseDto> findAll(int page, int size);
    Page<HostelResponseDto> search(HostelSearchDto search, int page, int size);
    HostelResponseDto findById(Long id);
    HostelResponseDto create(HostelRequestDto request);
    HostelResponseDto update(Long id, HostelRequestDto request);
    void delete(Long id);
}
