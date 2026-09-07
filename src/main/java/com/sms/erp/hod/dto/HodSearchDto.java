package com.sms.erp.hod.dto;

import com.sms.enums.RecordStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HodSearchDto {

    private String keyword;

    private String code;

    private RecordStatus status;
}
