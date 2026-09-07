package com.sms.erp.research.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResearchSearchDto {
    private String code;
    private String status;
    private String keyword;
}
