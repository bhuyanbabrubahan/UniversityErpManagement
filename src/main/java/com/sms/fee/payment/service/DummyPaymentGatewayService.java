package com.sms.fee.payment.service;

import com.sms.fee.payment.dto.DummyPaymentRequestDto;
import com.sms.fee.payment.dto.DummyPaymentResponseDto;

public interface DummyPaymentGatewayService {
    DummyPaymentResponseDto initiate(DummyPaymentRequestDto request);
}
