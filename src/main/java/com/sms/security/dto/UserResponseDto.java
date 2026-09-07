package com.sms.security.dto;

import java.time.LocalDateTime;

import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String mobileNumber;
    private Role role;
    private UserStatus status;
    private Boolean emailVerified;
    private Boolean mobileVerified;
    private Integer failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
}
