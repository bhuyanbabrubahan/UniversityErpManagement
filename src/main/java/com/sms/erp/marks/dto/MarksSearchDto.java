package com.sms.erp.marks.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MarksSearchDto {
    private String code;
    private String status;
    private String keyword;
}
