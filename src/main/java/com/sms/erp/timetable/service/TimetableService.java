package com.sms.erp.timetable.service;

import com.sms.erp.timetable.dto.*;
import org.springframework.data.domain.Page;

public interface TimetableService {
    Page<TimetableResponseDto> findAll(int page, int size);
    Page<TimetableResponseDto> search(TimetableSearchDto search, int page, int size);
    TimetableResponseDto findById(Long id);
    TimetableResponseDto create(TimetableRequestDto request);
    TimetableResponseDto update(Long id, TimetableRequestDto request);
    void delete(Long id);
}
