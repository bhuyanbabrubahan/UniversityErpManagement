package com.sms.erp.placement.service;

import com.sms.erp.placement.dto.*;
import org.springframework.data.domain.Page;

public interface PlacementService {
    Page<PlacementResponseDto> findAll(int page, int size);
    Page<PlacementResponseDto> search(PlacementSearchDto search, int page, int size);
    PlacementResponseDto findById(Long id);
    PlacementResponseDto create(PlacementRequestDto request);
    PlacementResponseDto update(Long id, PlacementRequestDto request);
    void delete(Long id);
}
