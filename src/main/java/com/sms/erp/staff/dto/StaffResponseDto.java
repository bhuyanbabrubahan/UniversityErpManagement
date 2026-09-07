package com.sms.erp.staff.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StaffResponseDto {
    private Long id;
    private String code;
    private String employeeCode;
    private String fullName;
    private String department;
    private String email;
    private String phone;
    private String designation;
    private String status;
    private String description;
    private String createdBy;
    private String updatedBy;
}
