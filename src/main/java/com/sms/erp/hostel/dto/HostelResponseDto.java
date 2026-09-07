package com.sms.erp.hostel.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HostelResponseDto {
    private Long id;
    private String code;
    private String hostelName;
    private String roomNo;
    private Integer capacity;
    private Integer occupied;
    private java.math.BigDecimal bedsFee;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
