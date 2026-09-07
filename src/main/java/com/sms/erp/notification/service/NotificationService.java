package com.sms.erp.notification.service;

import com.sms.erp.notification.dto.*;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<NotificationResponseDto> findAll(int page, int size);
    Page<NotificationResponseDto> search(NotificationSearchDto search, int page, int size);
    NotificationResponseDto findById(Long id);
    NotificationResponseDto create(NotificationRequestDto request);
    NotificationResponseDto update(Long id, NotificationRequestDto request);
    void delete(Long id);
}
