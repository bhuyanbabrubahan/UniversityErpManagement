package com.sms.erp.installment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallmentRequestDto {
    private String code;
    private String student;
    private String feeHead;
    private Integer installmentNo;
    private java.time.LocalDate dueDate;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal paidAmount;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
