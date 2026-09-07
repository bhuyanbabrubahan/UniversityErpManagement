package com.sms.security.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountStatusRequestDto {
    @NotBlank
    private String reason;
}
