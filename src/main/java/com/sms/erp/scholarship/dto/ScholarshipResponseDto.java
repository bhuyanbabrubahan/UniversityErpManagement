package com.sms.erp.scholarship.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScholarshipResponseDto {
    private Long id;
    private String code;
    private String student;
    private String scholarshipName;
    private String provider;
    private java.math.BigDecimal amount;
    private String academicYear;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
