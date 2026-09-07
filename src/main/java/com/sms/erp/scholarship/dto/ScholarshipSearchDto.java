package com.sms.erp.scholarship.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScholarshipSearchDto {
    private String code;
    private String status;
    private String keyword;
}
