package com.sms.scholarship.service;


import com.sms.dto.PageResponse;
import com.sms.scholarship.dto.StudentScholarshipRequestDto;
import com.sms.scholarship.dto.StudentScholarshipResponseDto;
import com.sms.scholarship.dto.StudentScholarshipSearchRequest;


public interface StudentScholarshipService {


    // Create scholarship request
    StudentScholarshipResponseDto createScholarship(
            StudentScholarshipRequestDto dto
    );


    // Get scholarship details by id
    StudentScholarshipResponseDto getScholarshipById(
            Long id
    );


    // Update scholarship details
    StudentScholarshipResponseDto updateScholarship(
            Long id,
            StudentScholarshipRequestDto dto
    );


    // Approve scholarship request by admin
    StudentScholarshipResponseDto approveScholarship(
            Long id
    );


    // Reject scholarship request by admin
    StudentScholarshipResponseDto rejectScholarship(
            Long id,
            String reason
    );


    // Soft delete scholarship
    void deleteScholarship(
            Long id
    );


    // Get all scholarships with pagination and sorting
    PageResponse<StudentScholarshipResponseDto> getAllScholarships(
            int page,
            int size,
            String sortBy,
            String direction
    );


    // Search scholarships with dynamic filters
    PageResponse<StudentScholarshipResponseDto> searchScholarships(
            StudentScholarshipSearchRequest request,
            int page,
            int size,
            String sortBy,
            String direction
    );

}