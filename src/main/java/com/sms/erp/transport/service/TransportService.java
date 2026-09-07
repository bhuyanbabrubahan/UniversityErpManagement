package com.sms.erp.transport.service;

import com.sms.erp.transport.dto.*;
import org.springframework.data.domain.Page;

public interface TransportService {
    Page<TransportResponseDto> findAll(int page, int size);
    Page<TransportResponseDto> search(TransportSearchDto search, int page, int size);
    TransportResponseDto findById(Long id);
    TransportResponseDto create(TransportRequestDto request);
    TransportResponseDto update(Long id, TransportRequestDto request);
    void delete(Long id);
}
