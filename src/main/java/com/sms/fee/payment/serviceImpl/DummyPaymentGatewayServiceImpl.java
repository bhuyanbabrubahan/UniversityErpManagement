package com.sms.fee.payment.serviceImpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.sms.fee.payment.dto.DummyPaymentRequestDto;
import com.sms.fee.payment.dto.DummyPaymentResponseDto;
import com.sms.fee.payment.service.DummyPaymentGatewayService;

@Service
public class DummyPaymentGatewayServiceImpl implements DummyPaymentGatewayService {
    @Override
    public DummyPaymentResponseDto initiate(DummyPaymentRequestDto request) {
        String transactionId = "DUMMY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        return new DummyPaymentResponseDto(transactionId, request.getPaymentId(), request.getAmount(), request.getPaymentMode().name(), "SUCCESS", "Dummy payment accepted. No real money was charged.");
    }
}
