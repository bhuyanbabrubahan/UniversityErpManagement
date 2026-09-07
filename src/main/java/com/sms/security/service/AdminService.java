package com.sms.security.service;

import com.sms.dto.PageResponse;
import com.sms.security.dto.AdminRequestDto;
import com.sms.security.dto.AdminResponseDto;
import com.sms.security.dto.AdminSearchRequestDto;

public interface AdminService {

    AdminResponseDto createAdmin(
            AdminRequestDto request
    );

    AdminResponseDto getAdminById(
            Long id
    );

    PageResponse<AdminResponseDto> getAllAdmins(
            int page,
            int size,
            String sortBy,
            String direction
    );

    PageResponse<AdminResponseDto> searchAdmins(
            AdminSearchRequestDto request,
            int page,
            int size,
            String sortBy,
            String direction
    );

    AdminResponseDto updateAdmin(
            Long id,
            AdminRequestDto request
    );

    void deleteAdmin(
            Long id
    );

}