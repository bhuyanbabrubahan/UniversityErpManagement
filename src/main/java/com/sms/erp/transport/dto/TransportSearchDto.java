package com.sms.erp.transport.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransportSearchDto {
    private String code;
    private String status;
    private String keyword;
}
