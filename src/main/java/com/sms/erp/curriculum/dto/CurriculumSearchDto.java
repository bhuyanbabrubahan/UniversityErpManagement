package com.sms.erp.curriculum.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CurriculumSearchDto {
    private String code;
    private String status;
    private String keyword;
}
