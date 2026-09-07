package com.sms.erp.staff.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StaffSearchDto {
    private String code;
    private String status;
    private String keyword;
}
