package com.sms.service;

import com.sms.dto.DepartmentRequestDto;
import com.sms.dto.DepartmentResponseDto;
import com.sms.dto.DepartmentSearchRequest;
import com.sms.dto.PageResponse;

public interface DepartmentService {

    DepartmentResponseDto createDepartment(DepartmentRequestDto dto);

    DepartmentResponseDto getDepartmentById(Long id);
    
    PageResponse<DepartmentResponseDto> getAllDepartments(int page, int size, String sortBy, String direction);
    
    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto);

    void deleteDepartment(Long id);
    
    PageResponse<DepartmentResponseDto> searchDepartments(
	        DepartmentSearchRequest request,
	        int page,
	        int size,
	        String sortBy,
	        String direction);
}
