package com.sms.erp.installment.service;

import com.sms.erp.installment.dto.*;
import org.springframework.data.domain.Page;

public interface InstallmentService {
    Page<InstallmentResponseDto> findAll(int page, int size);
    Page<InstallmentResponseDto> search(InstallmentSearchDto search, int page, int size);
    InstallmentResponseDto findById(Long id);
    InstallmentResponseDto create(InstallmentRequestDto request);
    InstallmentResponseDto update(Long id, InstallmentRequestDto request);
    void delete(Long id);
}
