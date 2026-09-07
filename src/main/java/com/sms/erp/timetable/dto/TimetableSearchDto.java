package com.sms.erp.timetable.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TimetableSearchDto {
    private String code;
    private String status;
    private String keyword;
}
