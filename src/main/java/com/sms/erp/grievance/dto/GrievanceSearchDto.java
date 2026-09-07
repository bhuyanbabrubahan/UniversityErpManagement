package com.sms.erp.grievance.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GrievanceSearchDto {
    private String code;
    private String status;
    private String keyword;
}
