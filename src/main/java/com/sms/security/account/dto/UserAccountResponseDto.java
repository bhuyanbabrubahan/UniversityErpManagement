package com.sms.security.account.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UserAccountResponseDto {
    private Long id;
    private String email;
    private String mobileNumber;
    private String role;
    private String status;
    private boolean emailVerified;
    private boolean mobileVerified;
    private LocalDateTime accountLockedUntil;
}
