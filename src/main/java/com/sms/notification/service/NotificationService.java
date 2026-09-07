package com.sms.notification.service;

import com.sms.notification.dto.NotificationRequestDto;

public interface NotificationService {
    void send(NotificationRequestDto request);
}
