package com.sms.fee.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sms.fee.payment.dto.DummyPaymentRequestDto;
import com.sms.fee.payment.dto.DummyPaymentResponseDto;
import com.sms.fee.payment.service.DummyPaymentGatewayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-gateway/dummy")
public class DummyPaymentGatewayController {
    private final DummyPaymentGatewayService service;

    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','ACCOUNTANT','STUDENT')")
    public ResponseEntity<DummyPaymentResponseDto> initiate(@Valid @RequestBody DummyPaymentRequestDto request) {
        return ResponseEntity.ok(service.initiate(request));
    }
}
