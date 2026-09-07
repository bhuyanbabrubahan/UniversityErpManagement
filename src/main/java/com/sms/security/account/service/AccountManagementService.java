package com.sms.security.account.service;

import org.springframework.data.domain.Page;
import com.sms.security.account.dto.UserAccountResponseDto;

public interface AccountManagementService {
    Page<UserAccountResponseDto> search(int page, int size, String keyword, String role, String status);
    UserAccountResponseDto block(Long id, String reason);
    UserAccountResponseDto unblock(Long id, String reason);
}
