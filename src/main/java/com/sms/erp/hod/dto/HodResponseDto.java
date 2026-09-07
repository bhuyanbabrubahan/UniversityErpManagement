package com.sms.erp.hod.dto;

import java.time.LocalDateTime;

import com.sms.enums.RecordStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HodResponseDto {

    private Long id;

    private String code;

    private String employeeCode;

    private String fullName;

    private String department;

    private String email;

    private String phone;

    private String designation;

    private RecordStatus status;

    private String description;

    private Long createdByUserId;

    private String createdBy;

    private Long updatedByUserId;

    private String updatedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
