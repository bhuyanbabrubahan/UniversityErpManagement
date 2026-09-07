package com.sms.erp.notification.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationSearchDto {
    private String code;
    private String status;
    private String keyword;
}
