package com.sms.erp.research.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResearchRequestDto {
    private String code;
    private String title;
    private String principalInvestigator;
    private String department;
    private java.math.BigDecimal grantAmount;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
