package com.sms.fee.payment.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DummyPaymentResponseDto {
    private String transactionId;
    private Long paymentId;
    private BigDecimal amount;
    private String paymentMode;
    private String status;
    private String message;
}
