package com.sms.erp.certificate.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CertificateSearchDto {
    private String code;
    private String status;
    private String keyword;
}
