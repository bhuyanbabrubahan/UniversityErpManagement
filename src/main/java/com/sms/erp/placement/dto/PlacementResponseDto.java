package com.sms.erp.placement.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlacementResponseDto {
    private Long id;
    private String code;
    private String companyName;
    private java.time.LocalDate driveDate;
    private String jobTitle;
    private java.math.BigDecimal packageAmount;
    private String eligibility;
    private Integer selectedStudents;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
