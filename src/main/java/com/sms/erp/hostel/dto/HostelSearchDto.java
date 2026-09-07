package com.sms.erp.hostel.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HostelSearchDto {
    private String code;
    private String status;
    private String keyword;
}
