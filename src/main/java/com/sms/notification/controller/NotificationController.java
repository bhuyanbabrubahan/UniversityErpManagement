package com.sms.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.notification.dto.NotificationRequestDto;
import com.sms.notification.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("notificationSendController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTANT','STAFF')")
    public ResponseEntity<Void> send(
            @Valid @RequestBody NotificationRequestDto request) {

        notificationService.send(request);

        return ResponseEntity.accepted().build();
    }
}
