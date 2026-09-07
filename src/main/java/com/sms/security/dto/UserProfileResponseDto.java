package com.sms.security.dto;

import com.sms.security.enums.UserStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "UserProfileResponseDto",
    description = "User information associated with a student."
)
public class UserProfileResponseDto {

    private Long id;

    private String email;

    private String mobileNumber;

    private Boolean emailVerified;

    private Boolean mobileVerified;

    private UserStatus status;
}
