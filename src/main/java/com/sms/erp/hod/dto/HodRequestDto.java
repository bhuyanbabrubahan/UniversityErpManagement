package com.sms.erp.hod.dto;

import com.sms.enums.RecordStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HodRequestDto {

    private String code;

    private String employeeCode;

    private String fullName;

    private String department;

    private String email;

    private String phone;

    private String designation;

    private RecordStatus status;

    private String description;
}
