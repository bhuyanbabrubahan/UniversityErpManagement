package com.sms.erp.installment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstallmentSearchDto {
    private String code;
    private String status;
    private String keyword;
}
