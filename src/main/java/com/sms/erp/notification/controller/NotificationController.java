package com.sms.erp.notification.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sms.erp.notification.dto.NotificationRequestDto;
import com.sms.erp.notification.dto.NotificationResponseDto;
import com.sms.erp.notification.dto.NotificationSearchDto;
import com.sms.erp.notification.service.NotificationService;

@RestController("erpNotificationController")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','REGISTRAR')")
@RequestMapping("/api/erp/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public Page<NotificationResponseDto> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.findAll(page, size);
    }

    @PostMapping("/search")
    public Page<NotificationResponseDto> search(
            @RequestBody(required = false) NotificationSearchDto search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.search(search, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> get(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDto> create(
            @RequestBody NotificationRequestDto request) {

        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> update(
            @PathVariable Long id,
            @RequestBody NotificationRequestDto request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
