package com.sms.fee.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeeActivityItemDto {
    private Long id;
    private String reference;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private String status;
}
