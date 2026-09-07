package com.sms.fee.payment.dto;

import java.math.BigDecimal;
import com.sms.fee.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DummyPaymentRequestDto {
    @NotNull private Long paymentId;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    @NotNull private PaymentMode paymentMode;
}
