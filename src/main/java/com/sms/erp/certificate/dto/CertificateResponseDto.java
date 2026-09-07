package com.sms.erp.certificate.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CertificateResponseDto {
    private Long id;
    private String code;
    private String student;
    private String certificateType;
    private java.time.LocalDate requestDate;
    private java.time.LocalDate issuedDate;
    private String certificateNo;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
