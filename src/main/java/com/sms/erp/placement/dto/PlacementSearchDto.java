package com.sms.erp.placement.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlacementSearchDto {
    private String code;
    private String status;
    private String keyword;
}
