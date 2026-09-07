package com.sms.security.dto;

import com.sms.security.enums.Role;
import com.sms.security.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserSummaryDto {

	private Long id;
	private String email;
	private String mobileNumber;
	private Role role;
	private UserStatus status;
	private boolean emailVerified;
	private boolean mobileVerified;
}
