package com.sms.security.service;

import com.sms.dto.PageResponse;
import com.sms.security.dto.SuperAdminRequestDto;
import com.sms.security.dto.SuperAdminResponseDto;
import com.sms.security.dto.SuperAdminSearchRequestDto;

public interface SuperAdminService {

    SuperAdminResponseDto createSuperAdmin(
            SuperAdminRequestDto request
    );

    SuperAdminResponseDto getSuperAdminById(
            Long id
    );

    PageResponse<SuperAdminResponseDto> getAllSuperAdmins(
            int page,
            int size,
            String sortBy,
            String direction
    );

    PageResponse<SuperAdminResponseDto> searchSuperAdmins(
            SuperAdminSearchRequestDto request,
            int page,
            int size,
            String sortBy,
            String direction
    );

    SuperAdminResponseDto updateSuperAdmin(
            Long id,
            SuperAdminRequestDto request
    );

    void deleteSuperAdmin(
            Long id
    );

}