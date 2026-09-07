package com.sms.security.dto;

import com.sms.enums.RecordStatus;
import com.sms.security.enums.SuperAdminDesignation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperAdminSearchRequestDto {

    private String keyword;

    private SuperAdminDesignation designation;

    private RecordStatus status;

}