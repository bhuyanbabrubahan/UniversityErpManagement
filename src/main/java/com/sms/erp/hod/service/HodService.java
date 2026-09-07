package com.sms.erp.hod.service;

import org.springframework.data.domain.Page;

import com.sms.erp.hod.dto.HodRequestDto;
import com.sms.erp.hod.dto.HodResponseDto;
import com.sms.erp.hod.dto.HodSearchDto;

public interface HodService {

    Page<HodResponseDto> findAll(
            int page,
            int size
    );

    Page<HodResponseDto> search(
            HodSearchDto search,
            int page,
            int size
    );

    HodResponseDto findById(
            Long id
    );

    HodResponseDto create(
            HodRequestDto request
    );

    HodResponseDto update(
            Long id,
            HodRequestDto request
    );

    void delete(
            Long id
    );
}